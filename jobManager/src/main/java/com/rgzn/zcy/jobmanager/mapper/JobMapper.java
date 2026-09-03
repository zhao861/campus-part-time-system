package com.rgzn.zcy.jobmanager.mapper;


import com.rgzn.zcy.jobmanager.bean.Job;
import com.rgzn.zcy.jobmanager.bean.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface JobMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into job(name, publisher_name, salary, create_time, update_time, audit_status)" +
            "values (#{name}, #{publisherName}, #{salary}, #{createTime}, #{updateTime}, #{auditStatus})")
    public Integer insertJob(Job job);

    @Delete("DELETE FROM job WHERE name = #{name} AND publisher_name = #{publisherName}")
    public Integer deleteJob(@Param("name") String name,
                             @Param("publisherName") String publisherName);

    @Select("SELECT COUNT(*) FROM job WHERE name = #{name} and publisher_name = #{publisher_name}")
    public Integer isExit(@Param("name") String name,
                          @Param("publisher_name") String publisherName);

    /** 按名称+发布者取一条记录 id（modify 重提时定位记录用，同名多行取最早一条） */
    @Select("SELECT id FROM job WHERE name = #{name} AND publisher_name = #{publisherName} ORDER BY id LIMIT 1")
    public Integer selectIdByNameAndPublisher(@Param("name") String name,
                                              @Param("publisherName") String publisherName);

    /** 商家修改重提：按 id 更新内容并重置审核状态、清空 AI 字段 */
    @Update("UPDATE job SET name = #{name}, salary = #{salary}, update_time = #{updateTime}, " +
            "audit_status = 0, risk_level = NULL, ai_reason = NULL, ai_model = NULL, ai_audit_time = NULL, " +
            "reviewer_name = NULL, review_time = NULL WHERE id = #{id}")
    public Integer updateJobContent(Job job);

    /** AI 初审后回写审核结论 */
    @Update("UPDATE job SET audit_status = #{auditStatus}, risk_level = #{riskLevel}, ai_reason = #{aiReason}, " +
            "ai_model = #{aiModel}, ai_audit_time = #{aiAuditTime} WHERE id = #{id}")
    public Integer updateAudit(Job job);

    /** 职位浏览：仅返回已发布（AI直过或人工通过） */
    @Select("select * from job where audit_status = 1")
    public List<Job> jobList();

    @Select("select * from job where publisher_name = #{publisherName}")
    public List<Job> jobListByPublisherName(String publisherName);

    /** 职位搜索：仅返回已发布 */
    @Select("select * from job where audit_status = 1 and name like concat('%', #{name}, '%')")
    public List<Job> jobListByLikeName(String name);

    /** 管理员复审：待人工审核列表 */
    @Select("select * from job where audit_status = 2 order by create_time asc")
    public List<Job> manualPendingList();

    /** 管理员复审通过（条件更新防重复操作） */
    @Update("UPDATE job SET audit_status = 1, reviewer_name = #{reviewerName}, review_time = #{reviewTime} " +
            "WHERE id = #{jobId} AND audit_status = 2")
    public Integer manualPass(@Param("jobId") Integer jobId,
                              @Param("reviewerName") String reviewerName,
                              @Param("reviewTime") LocalDateTime reviewTime);

    /** 管理员驳回（理由必填，覆盖 ai_reason 作为最终意见） */
    @Update("UPDATE job SET audit_status = 3, ai_reason = #{reason}, reviewer_name = #{reviewerName}, " +
            "review_time = #{reviewTime} WHERE id = #{jobId} AND audit_status = 2")
    public Integer manualReject(@Param("jobId") Integer jobId,
                                @Param("reason") String reason,
                                @Param("reviewerName") String reviewerName,
                                @Param("reviewTime") LocalDateTime reviewTime);

}
