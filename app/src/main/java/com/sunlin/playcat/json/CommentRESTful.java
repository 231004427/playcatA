package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.Comment;
import com.sunlin.playcat.domain.CommentList;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/25.
 */

public class CommentRESTful extends ObjRESTful {
    private static final String TAG="CommentRESTful";

    public CommentRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public CommentRESTful(User user){
        super(user);
    }
    public void search(CommentList commentList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(commentList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.COMMENT_SEARCH);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void add(Comment comment, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(comment);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.COMMENT_ADD);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
