package net.rubrion.server.disconnect;

import java.util.List;

public class KickMessage {

    protected List<String> message;
    protected String title;
    protected String description;
    protected String reason;
    protected List<String> steps;
    protected String author;
    protected int erroredStep;
    protected int errorCode;

}
