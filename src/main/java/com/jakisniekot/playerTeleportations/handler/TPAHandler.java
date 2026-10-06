package com.jakisniekot.playerTeleportations.handler;

import com.jakisniekot.playerTeleportations.PlayerTeleportations;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TPAHandler {

    private static final long REQUEST_TIMEOUT_TICKS = 20L * 60; // 60 sekund

    private final PlayerTeleportations plugin;

    /** Klucz: UUID odbiorcy (target). Jedna oczekująca prośba na odbiorcę. */
    private final Map<UUID, TPARequest> incomingByTarget = new HashMap<>();

    /** Klucz: UUID wysyłającego (requester). Jedna wysłana prośba na gracza. */
    private final Map<UUID, TPARequest> outgoingByRequester = new HashMap<>();

    public TPAHandler(PlayerTeleportations plugin) {
        this.plugin = plugin;
    }

    public TPARequestError sendRequest(Player requester, Player target) {
        if (requester == null || !requester.isOnline()) return TPARequestError.REQUESTER_OFFLINE;
        if (target == null || !target.isOnline()) return TPARequestError.TARGET_OFFLINE;
        if (target.getUniqueId().equals(requester.getUniqueId())) return TPARequestError.SELF_REQUEST;

        UUID requesterId = requester.getUniqueId();
        UUID targetId = target.getUniqueId();

        // Anuluj poprzednią prośbę wysłaną przez tego gracza (do kogokolwiek)
        cancelOutgoing(requesterId);

        // Opcjonalnie: odrzuć, jeśli target ma już oczekującą prośbę od kogoś innego
        // if (incomingByTarget.containsKey(targetId)) return TPARequestError.TARGET_BUSY;
        // Domyślnie: nowa prośba zastępuje starą u odbiorcy
        removeRequest(incomingByTarget.get(targetId));

        BukkitTask timeout = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            TPARequest current = incomingByTarget.get(targetId);
            if (current != null && current.requesterId.equals(requesterId)) {
                removeRequest(current);

            }
        }, REQUEST_TIMEOUT_TICKS);

        TPARequest request = new TPARequest(requesterId, targetId, timeout);
        incomingByTarget.put(targetId, request);
        outgoingByRequester.put(requesterId, request);

        return TPARequestError.NONE;
    }

    public TPARequestError denyRequest(Player answerer) {
        if (answerer == null) return TPARequestError.ANSWERER_OFFLINE;

        TPARequest request = incomingByTarget.get(answerer.getUniqueId());
        if (request == null) return TPARequestError.NO_REQUEST;

        // Prośbę zawsze usuwamy, niezależnie od stanu nadawcy
        removeRequest(request);

        return TPARequestError.NONE;
    }

    public TPARequestError acceptRequest(Player answerer) {
        if (answerer == null) return TPARequestError.ANSWERER_OFFLINE;

        TPARequest request = incomingByTarget.get(answerer.getUniqueId());
        if (request == null) return TPARequestError.NO_REQUEST;

        removeRequest(request);

        Player requester = Bukkit.getPlayer(request.requesterId);
        if (requester == null || !requester.isOnline()) return TPARequestError.REQUESTER_OFFLINE;

        teleportSequence(requester, answerer);
        return TPARequestError.NONE;
    }

    /** Anuluje prośbę wysłaną przez gracza (np. komenda /tpacancel). */
    public TPARequestError cancelRequest(Player requester) {
        if (requester == null) return TPARequestError.REQUESTER_OFFLINE;
        TPARequest request = outgoingByRequester.get(requester.getUniqueId());
        if (request == null) return TPARequestError.NO_REQUEST;
        removeRequest(request);
        return TPARequestError.NONE;
    }

    /** Wywołaj z PlayerQuitEvent. */
    public void handlePlayerQuit(UUID playerId) {
        cancelOutgoing(playerId);
        removeRequest(incomingByTarget.get(playerId));
    }

    public void teleportSequence(Player requester, Player target) {
        requester.teleport(target);
        Map<String, String> placeholder = new HashMap<>();
        placeholder.put("%player%", requester.getName());
        placeholder.put("%target%", target.getName());
        plugin.playerMessage(requester, "tpaccept.sender", placeholder);
        plugin.playerMessage(target, "tpaccept.target", placeholder);
    }

    // ---- helpers ----

    private void cancelOutgoing(UUID requesterId) {
        removeRequest(outgoingByRequester.get(requesterId));
    }

    private void removeRequest(TPARequest request) {
        if (request == null) return;
        request.timeoutTask.cancel();
        Player requester = Bukkit.getPlayer(request.requesterId);
        Player target = Bukkit.getPlayer(request.targetId);

        incomingByTarget.remove(request.targetId, request);
        outgoingByRequester.remove(request.requesterId, request);
    }

    private static final class TPARequest {
        final UUID requesterId;
        final UUID targetId;
        final BukkitTask timeoutTask;

        TPARequest(UUID requesterId, UUID targetId, BukkitTask timeoutTask) {
            this.requesterId = requesterId;
            this.targetId = targetId;
            this.timeoutTask = timeoutTask;
        }
    }
}