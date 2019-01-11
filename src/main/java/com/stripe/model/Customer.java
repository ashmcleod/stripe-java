package com.stripe.model;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Customer extends ApiResource implements HasId, MetadataStore<Customer> {
  /**
   * Current balance, if any, being stored on the customer's account. If negative, the customer has
   * credit to apply to the next invoice. If positive, the customer has an amount owed that will be
   * added to the next invoice. The balance does not refer to any unpaid invoices; it solely takes
   * into account amounts that have yet to be successfully applied to any invoice. This balance is
   * only taken into account as invoices are finalized. Note that the balance does not include
   * unpaid invoices.
   */
  Long accountBalance;

  /** Time at which the object was created. Measured in seconds since the Unix epoch. */
  Long created;

  /**
   * Three-letter [ISO code for the currency](https://stripe.com/docs/currencies) the customer can
   * be charged in for recurring billing purposes.
   */
  String currency;

  /** ID of the default source attached to this customer. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<PaymentSource> defaultSource;

  /** Always true for a deleted object. */
  Boolean deleted;

  /**
   * When the customer's latest invoice is billed by charging automatically, delinquent is true if
   * the invoice's latest charge is failed. When the customer's latest invoice is billed by sending
   * an invoice, delinquent is true if the invoice is not paid by its due date.
   */
  Boolean delinquent;

  /** An arbitrary string attached to the object. Often useful for displaying to users. */
  String description;

  /** Describes the current discount active on the customer, if there is one. */
  Discount discount;

  /** The customer's email address. */
  String email;

  /** Unique identifier for the object. */
  @Getter(onMethod = @__({@Override}))
  String id;

  /** The prefix for the customer used to generate unique invoice numbers. */
  String invoicePrefix;

  /**
   * Has the value `true` if the object exists in live mode or the value `false` if the object
   * exists in test mode.
   */
  Boolean livemode;

  /**
   * Set of key-value pairs that you can attach to an object. This can be useful for storing
   * additional information about the object in a structured format.
   */
  @Getter(onMethod = @__({@Override}))
  Map<String, String> metadata;

  /** String representing the object's type. Objects of the same type share the same value. */
  String object;

  /**
   * Mailing and shipping address for the customer. Appears on invoices emailed to this customer.
   */
  ShippingDetails shipping;

  /** The customer's payment sources, if any. */
  PaymentSourceCollection sources;

  /** The customer's current subscriptions, if any. */
  SubscriptionCollection subscriptions;

  /** The customer's tax information. Appears on invoices emailed to this customer. */
  TaxInfo taxInfo;

  /** Describes the status of looking up the tax ID provided in `tax_info`. */
  TaxInfoVerification taxInfoVerification;

  /** Get id of expandable `defaultSource` object. */
  public String getDefaultSource() {
    return (this.defaultSource != null) ? this.defaultSource.getId() : null;
  }

  public void setDefaultSource(String id) {
    this.defaultSource = ApiResource.setExpandableFieldId(id, this.defaultSource);
  }

  /** Get expanded `defaultSource`. */
  public PaymentSource getDefaultSourceObject() {
    return (this.defaultSource != null) ? this.defaultSource.getExpanded() : null;
  }

  public void setDefaultSourceObject(PaymentSource expandableObject) {
    this.defaultSource =
        new ExpandableField<PaymentSource>(expandableObject.getId(), expandableObject);
  }

  /**
   * Returns a list of your customers. The customers are returned sorted by creation date, with the
   * most recent customers appearing first.
   */
  public static CustomerCollection list(Map<String, Object> params) throws StripeException {
    return list(params, (RequestOptions) null);
  }

  /**
   * Returns a list of your customers. The customers are returned sorted by creation date, with the
   * most recent customers appearing first.
   */
  public static CustomerCollection list(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", Stripe.getApiBase(), "/v1/customers");
    return requestCollection(url, params, CustomerCollection.class, options);
  }

  /** Creates a new customer object. */
  public static Customer create(Map<String, Object> params) throws StripeException {
    return create(params, (RequestOptions) null);
  }

  /** Creates a new customer object. */
  public static Customer create(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", Stripe.getApiBase(), "/v1/customers");
    return request(ApiResource.RequestMethod.POST, url, params, Customer.class, options);
  }

  /**
   * Retrieves the details of an existing customer. You need only supply the unique customer
   * identifier that was returned upon customer creation.
   */
  public static Customer retrieve(String customer) throws StripeException {
    return retrieve(customer, (Map<String, Object>) null, (RequestOptions) null);
  }

  /**
   * Retrieves the details of an existing customer. You need only supply the unique customer
   * identifier that was returned upon customer creation.
   */
  public static Customer retrieve(String customer, RequestOptions options) throws StripeException {
    return retrieve(customer, (Map<String, Object>) null, options);
  }

  /**
   * Retrieves the details of an existing customer. You need only supply the unique customer
   * identifier that was returned upon customer creation.
   */
  public static Customer retrieve(
      String customer, Map<String, Object> params, RequestOptions options) throws StripeException {
    String url =
        String.format("%s%s", Stripe.getApiBase(), String.format("/v1/customers/%s", customer));
    return request(ApiResource.RequestMethod.GET, url, params, Customer.class, options);
  }

  /**
   * Updates the specified customer by setting the values of the parameters passed. Any parameters
   * not provided will be left unchanged. For example, if you pass the <strong>source</strong>
   * parameter, that becomes the customer’s active source (e.g., a card) to be used for all charges
   * in the future. When you update a customer to a new valid source: for each of the customer’s
   * current subscriptions, if the subscription bills automatically and is in the <code>past_due
   * </code> state, then the latest unpaid, unclosed invoice for the subscription will be retried
   * (note that this retry will not count as an automatic retry, and will not affect the next
   * regularly scheduled payment for the invoice). (Note also that no invoices pertaining to
   * subscriptions in the <code>unpaid</code> state, or invoices pertaining to canceled
   * subscriptions, will be retried as a result of updating the customer’s source.)
   *
   * <p>This request accepts mostly the same arguments as the customer creation call.
   */
  public Customer update(Map<String, Object> params) throws StripeException {
    return update(params, (RequestOptions) null);
  }

  /**
   * Updates the specified customer by setting the values of the parameters passed. Any parameters
   * not provided will be left unchanged. For example, if you pass the <strong>source</strong>
   * parameter, that becomes the customer’s active source (e.g., a card) to be used for all charges
   * in the future. When you update a customer to a new valid source: for each of the customer’s
   * current subscriptions, if the subscription bills automatically and is in the <code>past_due
   * </code> state, then the latest unpaid, unclosed invoice for the subscription will be retried
   * (note that this retry will not count as an automatic retry, and will not affect the next
   * regularly scheduled payment for the invoice). (Note also that no invoices pertaining to
   * subscriptions in the <code>unpaid</code> state, or invoices pertaining to canceled
   * subscriptions, will be retried as a result of updating the customer’s source.)
   *
   * <p>This request accepts mostly the same arguments as the customer creation call.
   */
  public Customer update(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format("%s%s", Stripe.getApiBase(), String.format("/v1/customers/%s", this.getId()));
    return request(ApiResource.RequestMethod.POST, url, params, Customer.class, options);
  }

  /**
   * Permanently deletes a customer. It cannot be undone. Also immediately cancels any active
   * subscriptions on the customer.
   */
  public Customer delete() throws StripeException {
    return delete((Map<String, Object>) null, (RequestOptions) null);
  }

  /**
   * Permanently deletes a customer. It cannot be undone. Also immediately cancels any active
   * subscriptions on the customer.
   */
  public Customer delete(RequestOptions options) throws StripeException {
    return delete((Map<String, Object>) null, options);
  }

  /**
   * Permanently deletes a customer. It cannot be undone. Also immediately cancels any active
   * subscriptions on the customer.
   */
  public Customer delete(Map<String, Object> params) throws StripeException {
    return delete(params, (RequestOptions) null);
  }

  /**
   * Permanently deletes a customer. It cannot be undone. Also immediately cancels any active
   * subscriptions on the customer.
   */
  public Customer delete(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format("%s%s", Stripe.getApiBase(), String.format("/v1/customers/%s", this.getId()));
    return request(ApiResource.RequestMethod.DELETE, url, params, Customer.class, options);
  }

  /** Removes the currently applied discount on a customer. */
  public Discount deleteDiscount() throws StripeException {
    return deleteDiscount((Map<String, Object>) null, (RequestOptions) null);
  }

  /** Removes the currently applied discount on a customer. */
  public Discount deleteDiscount(Map<String, Object> params) throws StripeException {
    return deleteDiscount(params, (RequestOptions) null);
  }

  /** Removes the currently applied discount on a customer. */
  public Discount deleteDiscount(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s", Stripe.getApiBase(), String.format("/v1/customers/%s/discount", this.getId()));
    return request(ApiResource.RequestMethod.DELETE, url, params, Discount.class, options);
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class TaxInfo extends StripeObject {
    /** The customer's tax ID number. */
    String taxId;

    /** The type of ID number. */
    String type;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class TaxInfoVerification extends StripeObject {
    /**
     * The state of verification for this customer. Possible values are `unverified`, `pending`, or
     * `verified`.
     */
    String status;

    /** The official name associated with the tax ID returned from the external provider. */
    String verifiedName;
  }
}
