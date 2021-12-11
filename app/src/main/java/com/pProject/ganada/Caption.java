package com.pProject.ganada;

public class Caption {
    private String kind;
    private String message;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Caption{" +
                "kind='" + kind + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
