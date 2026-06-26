package com.example.demo.controller;

import com.example.demo.entity.Address;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
  private final UserRepository users; private final AddressRepository addresses; private final OrderRepository orders;
  public ProfileController(UserRepository users, AddressRepository addresses, OrderRepository orders) { this.users=users; this.addresses=addresses; this.orders=orders; }
  @GetMapping public Map<String,Object> get(@RequestParam UUID userId, @RequestParam String email) {
    User user=users.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    Map<String,Object> result=new HashMap<>(); result.put("name",user.getName()); result.put("email",user.getEmail()); result.put("addresses",addresses.findByUserId(userId)); result.put("orders",orders.findByUserId(userId)); return result;
  }
  @PostMapping("/addresses") public Address add(@RequestParam UUID userId,@RequestBody Address address) { address.setAddressId(UUID.randomUUID()); address.setUserId(userId); return addresses.save(address); }
  @PutMapping("/addresses/{id}") public ResponseEntity<Address> update(@RequestParam UUID userId,@PathVariable UUID id,@RequestBody Address input) { Address address=addresses.findById(id).orElseThrow(); if(!userId.equals(address.getUserId())) return ResponseEntity.status(403).build(); input.setAddressId(id); input.setUserId(userId); return ResponseEntity.ok(addresses.save(input)); }
}
