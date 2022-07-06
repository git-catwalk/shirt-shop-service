package com.bluntsoftware.shirtshop.integrations.types.stripe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
https://dashboard.stripe.com/test/dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StripeCredentials {
    String publishableKey; // pk_test_51L4Ld9GDRbJBsrTYqZi4WGSjH3NTNEXqp06LAeQLmDSp3KfpIOc8Bd3KlkJcLHKCaB5qgNeDd8RJg94rsFNLdby200CnKXGWWh
    String secretKey; // sk_test_51L4Ld9GDRbJBsrTYxJqIknWTIgov61i1Lgfk8gA61hb4jJALsFJFQBiJbOcbUQbKpmxiWfv4QQiBYOL8S2rkBrc300UwXhWe1K
}
