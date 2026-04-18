package com.example.timetracker.mapper;

import com.example.timetracker.entity.Task;
import com.example.timetracker.entity.TaskStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TaskMapper {

    @Insert("""
            INSERT INTO tasks (title, description, status)
            VALUES (#{title}, #{description}, #{status})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Task task);

    @Select("""
            SELECT id, title, description, status
            FROM tasks
            WHERE id = #{id}
            """)
    Task findById(Long id);

    @Update("""
            UPDATE tasks
            SET status = #{status}
            WHERE id = #{id}
            """)
    int updateStatus(@Param("id") Long id, @Param("status") TaskStatus status);
}
