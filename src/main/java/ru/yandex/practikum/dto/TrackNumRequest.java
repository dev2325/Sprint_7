package ru.yandex.practikum.dto;

// описание сущности track-номера, который будет передаваться
public class TrackNumRequest {
    private Integer track;

    public TrackNumRequest(Integer track) {
        this.track = track;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }
}
