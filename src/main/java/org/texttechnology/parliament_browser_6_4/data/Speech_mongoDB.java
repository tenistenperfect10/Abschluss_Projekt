package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;

public interface Speech_mongoDB {

    String getSpeaker();

    void setSpeaker(String speaker);

    String getText();

    void setText(String text);

    List<String> getComments();

    void setComments(List<String> comments);
}
