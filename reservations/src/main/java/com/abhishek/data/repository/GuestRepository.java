package com.abhishek.data.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.abhishek.data.entity.Guest;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends PagingAndSortingRepository<Guest, Long> {
}