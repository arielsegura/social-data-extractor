package io.github.arielsegura.socialextractor.model;

public class SearchRequest {

    private String searchTerm;
    private boolean justRecents = true;
    private boolean includeComments;
    private boolean includeRetweeets = true;
    private int count = 800;
    private boolean refresh;

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean isJustRecents() {
        return justRecents;
    }

    public void setJustRecents(boolean justRecents) {
        this.justRecents = justRecents;
    }

    public boolean isIncludeComments() {
        return includeComments;
    }

    public void setIncludeComments(boolean includeComments) {
        this.includeComments = includeComments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }
}
