package uz.old.contactsync.process

enum class ProcessState {
    IN_PROGRESS,
    TRY_AGAIN,
    ERROR,
    COMPLETE,
    UNEXPECTED_BEHAVIOUR
}