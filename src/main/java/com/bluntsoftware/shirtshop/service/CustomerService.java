package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.integrations.types.quick_books.service.QuickbooksApiService;
import com.bluntsoftware.shirtshop.mapper.QBMapper;
import com.bluntsoftware.shirtshop.model.Customer;
import com.bluntsoftware.shirtshop.repository.CustomerRepo;
import com.intuit.ipp.exception.FMSException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class CustomerService{
  private final QuickbooksApiService quickbooksService;
  private final CustomerRepo repo;

  public CustomerService(QuickbooksApiService quickbooksService, CustomerRepo repo) {
    this.quickbooksService = quickbooksService;
    this.repo = repo;
  }

  public  Customer save(Customer item) {
    return repo.save(item);
  }

  public void deleteById(String id) {
      repo.deleteById(id);
  }

  public Optional<Customer> findById(String id) {
    return repo.findById(id);
  }

  public Iterable<Customer> findAll() {
    return repo.findAll();
  }

  public Page<Customer> search(String term,Pageable pageable) {
    return repo.findAllByNameIgnoreCaseContaining(term,pageable);
  }

  public void importFromQuickBooks() throws FMSException {
    this.quickbooksService.findAll().forEach(t->{
      Customer customer = repo.save(QBMapper.mapCustomer(t));
      try {
        t.setClientEntityId(customer.getId());
        this.quickbooksService.saveCustomer(t);
      } catch (FMSException e) {
        e.printStackTrace();
      }
    });
  }

  public void importCsv(InputStream is){

     try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
          CSVParser csvParser = new CSVParser(fileReader,
                 CSVFormat.Builder.create()
                         .setHeader().setSkipHeaderRecord(true)
                         .setIgnoreHeaderCase(false)
                         .setTrim(true)
                         .build())) {

            csvParser.getRecords().forEach(r-> save( Customer.builder()
               .name(r.get("customer_name"))
               .firstName(r.get("contact_first_name"))
               .lastName(r.get("contact_last_name"))
               .accountNumber(r.get("account_number"))
               .email(r.get("email"))
               .fax(r.get("fax"))
               .mobile(r.get("mobile"))
               .tollFree(r.get("toll_free"))
               .website(r.get("website"))
               .city(r.get("city"))
               .state(r.get("province/state"))
               .country(r.get("country"))
               .street1(r.get("address_line_1"))
               .street2(r.get("address_line_2"))
               .zipcode(r.get("postal_code/zip_code"))
               .shipContact(r.get("ship-to_address_line_1"))
               .shipPhone(r.get("ship-to_phone"))
               .shipStreet1(r.get("ship-to_address_line_1"))
               .shipStreet2(r.get("ship-to_address_line_2"))
               .shipCity(r.get("ship-to_city"))
               .shipState(r.get("ship-to_province/state"))
               .shipCountry(r.get("ship-to_country"))
               .shipZipcode(r.get("ship-to_postal_code/zip_code"))
               .deliveryInstructions(r.get("delivery_instructions"))
               .build())
        );
    } catch (Exception e) {
        throw new RuntimeException("Could not parse the csv file: " + e.getMessage());
    }
  }

}
