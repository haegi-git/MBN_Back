package com.example.mbn.community.repository;

import com.example.mbn.community.entity.CommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {

}