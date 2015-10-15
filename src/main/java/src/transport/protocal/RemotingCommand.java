package src.transport.protocal;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2015/8/25.
 */
public class RemotingCommand implements Serializable{

    //请求序列，用于生成请求序列
    private static AtomicInteger RequestId = new AtomicInteger(0);
    //请求序列号
    private int opaque = RequestId.getAndIncrement();
    //是否成功
    private boolean flag;

    private final int cmdCode;
    //请求类型 1.request 2.response
    private final int rpcType;
    //是否单向请求
    private final boolean rpcOneway;

    public static final int REQUEST_COMMAND = 1;
    public static final int RESPONSE_COMMAND = 2;

    //请求内容
    private String content;

    private CommandBody body;

    public RemotingCommand(int cmdCode, int rpcType, boolean rpcOneway) {
        this.cmdCode = cmdCode;
        this.rpcType = rpcType;
        this.rpcOneway = rpcOneway;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRpcType() {
        return rpcType;
    }

    public int getCmdCode() {
        return cmdCode;
    }

    public boolean isRpcOneway() {
        return rpcOneway;
    }

    public int getOpaque() {
        return opaque;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }

    public CommandBody getBody() {
        return body;
    }

    public void setBody(CommandBody body) {
        this.body = body;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
