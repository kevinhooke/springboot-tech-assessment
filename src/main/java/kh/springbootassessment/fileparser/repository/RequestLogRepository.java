package kh.springbootassessment.fileparser.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kh.springbootassessment.fileparser.data.RequestLogItem;

@Repository
public interface RequestLogRepository
    extends CrudRepository<RequestLogItem, Long> {
}