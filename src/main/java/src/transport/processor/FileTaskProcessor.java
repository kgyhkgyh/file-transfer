package src.transport.processor;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.io.FilenameUtils;
import src.transport.protocal.FileSegmentRequest;
import src.file.FileStorage;
import src.transport.protocal.FileTaskRequest;
import src.transport.netty.NettyRequestProcessor;
import src.transport.protocal.RemotingCommand;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2015/8/30.
 */
public class FileTaskProcessor implements NettyRequestProcessor {

    private ConcurrentHashMap<String, FileStorage> fileTable = new ConcurrentHashMap<>(16);

    private final String baseDir;

    public FileTaskProcessor(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        int cmdCode = request.getCmdCode();
        RemotingCommand response = null;
        if (cmdCode == 1) {
            response = processFileTaskRequest(request);
        }else if (cmdCode == 2) {
            processFileSegmentRequest(request);
        }
        return response;
    }

    private RemotingCommand processFileTaskRequest(RemotingCommand request) throws Exception {
        RemotingCommand response = new RemotingCommand(1, 2, false);
        if(fileTable.size() >= 16) {
            response.setFlag(false);
            return response;
        }
        FileTaskRequest body = (FileTaskRequest) request.getBody();
        String fileName = body.getFileName();
        String filePath = FilenameUtils.normalize(baseDir + File.separator + fileName);
        FileChannel fileChannel = new RandomAccessFile(filePath, "rw").getChannel();
        FileStorage fileStorage = new FileStorage(fileChannel, body.getFileSize(), body.getMd5(), body.getBlockSize());
        fileTable.put(fileName, fileStorage);
        response.setFlag(true);
        return response;
    }

    private void processFileSegmentRequest(RemotingCommand request) throws Exception {
        FileSegmentRequest body = (FileSegmentRequest) request.getBody();
        String fileName = body.getFileName();
        FileStorage fileStorage = fileTable.get(fileName);
        fileStorage.write(body.getPosition(), body.getContent());

        if(fileStorage.isFinished()) {
            System.out.println("完成");
            fileStorage.close();
            fileTable.remove(fileName);
        }
    }

}
