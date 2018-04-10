package com.ourblog.noyage;

import java.util.ArrayList;
import java.util.List;

public class Article {
    private String title;
    private String content;
    List<String> taglist;
    public Article(String titleInput, String contentInput,ArrayList<String> listInput) {
        title = titleInput;
        content = contentInput;
        taglist = listInput;

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
