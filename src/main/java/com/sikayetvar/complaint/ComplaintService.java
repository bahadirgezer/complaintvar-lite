package com.sikayetvar.complaint;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintByID(Long id) {
        Optional<Complaint> complaintByID = complaintRepository.findComplaintByID(id);

        if (!complaintByID.isPresent()) {
            throw new IllegalArgumentException();
        }
        return complaintByID.get();
    }

    public Complaint createComplaint(Complaint complaintRequest) {
        //TODO:add exception logic
        return complaintRepository.save(complaintRequest); //should not save request
    }

    public Complaint updateComplaint(long id, Complaint complaintRequest) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());

        complaint.setTitle(complaintRequest.getTitle());
        complaint.setBody(complaintRequest.getBody());
        complaint.setCompany(complaintRequest.getCompany());
        complaint.setUser(complaintRequest.getUser());
        //TODO:add time and user and company
        return complaintRepository.save(complaint);
    }

    public void deleteComplaint(long id) {
        Complaint post = complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        //TODO: Create a new exception
        complaintRepository.delete(post);
    }
}
