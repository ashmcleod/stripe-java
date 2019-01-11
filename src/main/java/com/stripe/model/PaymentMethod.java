package com.stripe.model;

import com.stripe.net.ApiResource;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PaymentMethod extends StripeObject implements PaymentSource {
  BillingDetails billingDetails;

  Card card;

  Long created;

  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Customer> customer;

  @Getter(onMethod = @__({@Override}))
  String id;

  Ideal ideal;

  Boolean livemode;

  Map<String, String> metadata;

  String object;

  SepaDebit sepaDebit;

  String type;

  /** Get id of expandable `customer` object. */
  public String getCustomer() {
    return (this.customer != null) ? this.customer.getId() : null;
  }

  public void setCustomer(String id) {
    this.customer = ApiResource.setExpandableFieldId(id, this.customer);
  }

  /** Get expanded `customer`. */
  public Customer getCustomerObject() {
    return (this.customer != null) ? this.customer.getExpanded() : null;
  }

  public void setCustomerObject(Customer expandableObject) {
    this.customer = new ExpandableField<Customer>(expandableObject.getId(), expandableObject);
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class BillingDetails extends StripeObject {
    Address address;

    String email;

    String name;

    String phone;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class Card extends StripeObject {
    String brand;

    Long expMonth;

    Long expYear;

    String funding;

    String last4;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class Ideal extends StripeObject {
    /** The customer's bank, if provided. */
    String bank;

    /** The Bank Identifier Code of the customer's bank, if the bank was provided. */
    String bic;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class SepaDebit extends StripeObject {
    String bankCode;

    String branchCode;

    String country;

    String fingerprint;

    String last4;
  }
}
