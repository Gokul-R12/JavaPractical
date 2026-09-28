package com.college.roombooking.Repository;

import com.college.roombooking.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GuestRepository extends  JpaRepository <Guest,Long > {

}
