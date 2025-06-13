package com.guo.mapper;

import com.guo.domain.Reservation;
import com.guo.domain.ReservationExample;
import com.guo.domain.Vo.ReservationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {
    long countByExample(ReservationExample example);

    int deleteByExample(ReservationExample example);

    /**
     * 选择性地插入一条预约记录。
     * @param record 预约记录对象
     * @return 受影响的行数
     */
    int insertSelective(Reservation record);

    /**
     * 根据主键ID查找预约记录。
     * @param reserveId 预约ID
     * @return Reservation对象
     */
    Reservation selectByPrimaryKey(Integer reserveId);

    /**
     * 根据主键ID选择性地更新预约记录。
     * @param record 包含更新信息的预约对象
     * @return 受影响的行数
     */
    int updateByPrimaryKeySelective(Reservation record);

    /**
     * 查找某本书最早的一个等待中的预约。
     * @param bookId 图书ID
     * @return Reservation对象，如果不存在则返回null
     */
    Reservation findNextPendingReservation(@Param("bookId") int bookId);

    /**
     * 统计一个用户对某本书的、状态为'pending'的预约数量。
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 预约数量
     */
    long countPendingReservationForUser(@Param("userId") int userId, @Param("bookId") int bookId);

    /**
     * 【新增】根据用户ID查询其所有的预约记录，并包含图书标题等详细信息。
     * @param userId 用户ID
     * @return 包含详细信息的预约记录VO列表
     */
    List<ReservationVo> findReservationsWithDetailsByUserId(int userId);

    /**
     * [NEW] Paginates and queries all reservation details.
     * @return A list of detailed reservation VOs.
     */
    List<ReservationVo> findAllWithDetailsByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * [NEW] Counts the total number of reservation records.
     * @return Total count of reservations.
     */
    long countAllReservations();
}