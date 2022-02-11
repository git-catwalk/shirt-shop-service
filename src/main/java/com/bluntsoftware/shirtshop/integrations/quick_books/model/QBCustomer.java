package com.bluntsoftware.shirtshop.integrations.quick_books.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QBCustomer {
    String FullyQualifiedName;
    String DisplayName;
    String Suffix;
    String Title;
    String MiddleName;
    String Notes;
    String FamilyName;
    String CompanyName;
    String GivenName;
    PrimaryEmailAddress PrimaryEmailAddr;
    BillAddress BillAddr;
}
