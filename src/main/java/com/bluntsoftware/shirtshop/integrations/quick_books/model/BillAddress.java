package com.bluntsoftware.shirtshop.integrations.quick_books.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillAddress {
    String CountrySubDivisionCode;
    String City;
    String PostalCode;
    String Line1;
    String Country;
}
