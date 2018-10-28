package io.github.aplotnikov.batch.processing.reactor;

import java.util.Objects;

class Response {

    private final long clientId;

    private final Status status;

    Response(long clientId, Status status) {
        this.clientId = clientId;
        this.status = status;
    }

    long getClientId() {
        return clientId;
    }

    Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Response that = (Response) other;
        return clientId == that.clientId &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, status);
    }

    @Override
    public String toString() {
        return "ClientResponse{" +
                "clientId=" + clientId +
                ", status=" + status +
                '}';
    }

    enum Status {
        SUCCESS,
        FAILED
    }
}
