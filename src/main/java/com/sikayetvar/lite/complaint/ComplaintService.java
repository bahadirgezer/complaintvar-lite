package com.sikayetvar.lite.complaint;

import com.sikayetvar.lite.company.Company;
import com.sikayetvar.lite.company.CompanyRepository;
import com.sikayetvar.lite.user.User;
import com.sikayetvar.lite.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

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

    public Complaint createComplaint(Complaint complaintRequest, Long user, Long company) {
        complaintRequest.setCompany(companyRepository.findCompanyByID(company)
                .orElseThrow(() -> new IllegalArgumentException()));
        complaintRequest.setUser(userRepository.findUserByID(user)
                .orElseThrow(() -> new IllegalArgumentException()));
        return complaintRepository.save(complaintRequest);
    }

    public Complaint linkUserAndCompany(Complaint complaint, Long user_id, Long company_id) {
        Company company = companyRepository.findCompanyByID(company_id)
                .orElseThrow(() -> new IllegalArgumentException());
        User user = userRepository.findUserByID(user_id)
                .orElseThrow(() -> new IllegalArgumentException());
        //TODO: should add more logic
        complaint.setUser(user);
        complaint.setCompany(company);
        return complaintRepository.save(complaint);
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
        Complaint user = complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        //TODO: Create a new exception
        complaintRepository.delete(user);
    }
}
