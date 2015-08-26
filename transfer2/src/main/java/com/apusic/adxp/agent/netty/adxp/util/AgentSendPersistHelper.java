///*
// *--------------------------------------
// * Apusic (Kingdee Middleware)
// *---------------------------------------
// * Copyright By Apusic ,All right Reserved
// * chenpengliang   2015-3-24   comment
// * chenpengliang  2015-3-24  Created
// */
//package com.apusic.adxp.agent.netty.adxp.util;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import com.apusic.adxp.agent.netty.adxp.modal.AgentTransferTask;
//import com.apusic.adxp.util.db.DBUtil;
//import com.apusic.adxp.util.string.StringUtil;
//
//public class AgentSendPersistHelper {
//
//    
//    
//    /**
//     * 添加传输任务
//     * @param conn
//     * @param relativeFilePath
//     * @param cmdId
//     * @param targetCodes
//     * @param weigth
//     * @param opts
//     * @param taskType
//     * @param instanceId
//     * @param sourceDir
//     * @throws Exception
//     */
//    public static String addSendTask(Connection conn, String relativeFilePath, String cmdId, String[] targetCodes, float weigth, String[] opts, String taskType, String instanceId, String sourceDir) throws Exception {
//        PreparedStatement stmt = null;
//        String id = UUID.randomUUID().toString();
//        try {
//            stmt = conn.prepareStatement("insert into t_adxp_transfer(c_relative_file_path, c_target_codes, c_weight, c_opts, c_createDate,c_status,c_source_dir,c_cmdId,c_task_type,c_instanceId,id) values(?,?,?,?,?,?,?,?,?,?,?)");
//            stmt.setString(1, relativeFilePath);
//            stmt.setString(2, StringUtil.join(targetCodes, ","));
//            stmt.setFloat(3, weigth);
//            stmt.setString(4, StringUtil.join(opts, ","));
//            stmt.setLong(5, new Date().getTime());
//            stmt.setString(6, "not_send");
//            stmt.setString(7, sourceDir);
//            stmt.setString(8, cmdId);
//            stmt.setString(9, taskType.toString());
//            stmt.setString(10, instanceId);
//            stmt.setString(11, id);
//            stmt.execute();
//            return id;
//        } catch (Exception e) {
//            throw new Exception(e);
//        } finally {
//            DBUtil.close(stmt);
//            DBUtil.close(conn);
//        }
//    }
//    
//    /**
//     * 修改传输任务状态，等待接收方确认
//     * @param id
//     * @param conn
//     * @throws Exception
//     */
//    public static void updateSendStatus(String id, Connection conn) throws Exception{
//        PreparedStatement stmt = null;
//        try {
//            String updateSql = "update t_adxp_transfer set c_status = 'sending' where id = ?";
//            stmt = conn.prepareStatement(updateSql);
//            stmt.setString(1, id);
//            stmt.execute();
//        } catch (Exception e) {
//            throw new Exception(e);
//        } finally {
//            DBUtil.close(stmt);
//            DBUtil.close(conn);
//        }
//    }
//    
//    /**
//     * 接收方确认后删除任务
//     * @param id
//     * @param conn
//     */
//    public static void finishSend(String id, Connection conn) throws Exception{
//        PreparedStatement stmt = null;
//        try {
//            String updateSql = "delete from  t_adxp_transfer where c_instanceid = ?";
//            stmt = conn.prepareStatement(updateSql);
//            stmt.setString(1, id);
//            stmt.execute();
//        } catch (Exception e) {
//            throw new Exception(e);
//        } finally {
//            DBUtil.close(stmt);
//            DBUtil.close(conn);
//        }
//    }
//    
//    /**
//     * 返回需要传输的任务（按照权重以及创建时间进行排序）
//     * @return
//     */
//    public static List<AgentTransferTask> listTasks(Connection conn)  throws Exception {
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        List<AgentTransferTask> tasks = new ArrayList<AgentTransferTask>();
//        try {
//            stmt = conn.prepareStatement("select id,c_relative_file_path,c_target_codes,c_weight,c_opts,c_source_dir,c_cmdId,c_task_type,c_instanceId from t_adxp_transfer where c_status='not_send' order by c_weight desc,c_createDate ");
//            rs = stmt.executeQuery();
//            while(rs.next()) {
//                AgentTransferTask task = new AgentTransferTask();
//                task.setId(rs.getString("id"));
//                task.setRelativeFilePath(rs.getString("c_relative_file_path"));
//                task.setSourceDir(rs.getString("c_source_dir"));
//                task.setCmdId(rs.getString("c_cmdId"));
//                task.setTargetCode(rs.getString("c_target_codes"));
//                task.setOpts(rs.getString("c_opts"));
//                task.setWeight(rs.getFloat("c_weight"));
//                task.setTaskType(rs.getString("c_task_type"));
//                task.setInstanceId(rs.getString("c_instanceId"));
//                tasks.add(task);
//            }
//        } catch (Exception e) {
//            throw new Exception(e);
//        } finally {
//            DBUtil.closeQuietly(conn, stmt, rs);
//        }
//        return tasks;
//    }
//}
