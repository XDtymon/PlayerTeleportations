package com.jakisniekot.playerTeleportations.handler;

public enum TPARequestError {
    SELF_REQUEST(),
    REQUESTER_OFFLINE(),
    NO_REQUEST(),
    ANSWERER_OFFLINE(),
    TARGET_OFFLINE(),
    NONE();
}
