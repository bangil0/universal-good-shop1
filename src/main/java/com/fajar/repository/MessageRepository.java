package com.fajar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.entity.Message;

public interface MessageRepository extends JpaRepository<Message		, Long>{
 

}
