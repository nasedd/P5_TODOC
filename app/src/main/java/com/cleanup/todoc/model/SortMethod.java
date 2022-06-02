package com.cleanup.todoc.model;

import com.cleanup.todoc.R;

public enum SortMethod {

    ALPHABETICAL(R.id.filter_alphabetical),

    ALPHABETICAL_INVERTED(R.id.filter_alphabetical_inverted),

    RECENT_FIRST(R.id.filter_recent_first),

    OLD_FIRST(R.id.filter_oldest_first),

    NONE(0);

    public int getFilterId() {
        return id;
    }

    private final int id;

    SortMethod(int id){
        this.id = id;
    }

    public static SortMethod findFilterById(int id){
        for(SortMethod sortMethod: values()){
            if(sortMethod.id == id){
                return sortMethod;
            }
        }
        return SortMethod.OLD_FIRST;
    }


}
