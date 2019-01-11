package com.stripe.model;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.radar.Rule;
import com.stripe.net.ApiResource;
import com.stripe.net.RequestOptions;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Charge extends ApiResource implements BalanceTransactionSource, MetadataStore<Charge> {
  AlternateStatementDescriptors alternateStatementDescriptors;

  /**
   * A positive integer in the [smallest currency
   * unit](https://stripe.com/docs/currencies#zero-decimal) (e.g., 100 cents to charge $1.00 or 100
   * to charge ¥100, a zero-decimal currency) representing how much to charge. The minimum amount is
   * $0.50 US or [equivalent in charge
   * currency](https://support.stripe.com/questions/what-is-the-minimum-amount-i-can-charge-with-stripe).
   */
  Long amount;

  /**
   * Amount in %s refunded (can be less than the amount attribute on the charge if a partial refund
   * was issued).
   */
  Long amountRefunded;

  /** ID of the Connect application that created the charge. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Application> application;

  /**
   * The application fee (if any) for the charge. [See the Connect
   * documentation](/docs/connect/direct-charges#collecting-fees) for details.
   */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<ApplicationFee> applicationFee;

  /** The amount of the application fee (if any) for the resulting payment. */
  Long applicationFeeAmount;

  /** Authorization code on the charge. */
  String authorizationCode;

  /**
   * ID of the balance transaction that describes the impact of this charge on your account balance
   * (not including refunds or disputes).
   */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<BalanceTransaction> balanceTransaction;

  /**
   * If the charge was created without capturing, this Boolean represents whether it is still
   * uncaptured or has since been captured.
   */
  Boolean captured;

  /** Time at which the object was created. Measured in seconds since the Unix epoch. */
  Long created;

  /**
   * Three-letter [ISO currency code](https://www.iso.org/iso-4217-currency-codes.html), in
   * lowercase. Must be a [supported currency](https://stripe.com/docs/currencies).
   */
  String currency;

  /** ID of the customer this charge is for if one exists. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Customer> customer;

  /** An arbitrary string attached to the object. Often useful for displaying to users. */
  String description;

  /**
   * The account (if any) the charge was made on behalf of, with an automatic transfer. [See the
   * Connect documentation](/docs/connect/destination-charges) for details.
   */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Account> destination;

  /** Details about the dispute if the charge has been disputed. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Dispute> dispute;

  /**
   * Error code explaining reason for charge failure if available (see [the errors
   * section](/docs/api#errors) for a list of codes).
   */
  String failureCode;

  /** Message to user further explaining reason for charge failure if available. */
  String failureMessage;

  /** Information on fraud assessments for the charge. */
  FraudDetails fraudDetails;

  /** Unique identifier for the object. */
  @Getter(onMethod = @__({@Override}))
  String id;

  /** ID of the invoice this charge is for if one exists. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Invoice> invoice;

  Level3 level3;

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
   * The account (if any) the charge was made on behalf of without triggering an automatic transfer.
   * See the [Connect documentation](/docs/connect/charges-transfers) for details.
   */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Account> onBehalfOf;

  /** ID of the order this charge is for if one exists. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Order> order;

  /**
   * Details about whether the payment was accepted, and why. See [understanding
   * declines](/docs/declines) for details.
   */
  Outcome outcome;

  /** `true` if the charge succeeded, or was successfully authorized for later capture. */
  Boolean paid;

  /** ID of the PaymentIntent associated with this charge, if one exists. */
  String paymentIntent;

  /** This is the email address that the receipt for this charge was sent to. */
  String receiptEmail;

  /**
   * This is the transaction number that appears on email receipts sent for this charge. This
   * attribute will be `null` until a receipt has been sent.
   */
  String receiptNumber;

  /**
   * Whether the charge has been fully refunded. If the charge is only partially refunded, this
   * attribute will still be false.
   */
  Boolean refunded;

  /** A list of refunds that have been applied to the charge. */
  RefundCollection refunds;

  /** ID of the review associated with this charge if one exists. */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Review> review;

  /** Shipping information for the charge. */
  ShippingDetails shipping;

  PaymentSource source;

  /**
   * The transfer ID which created this charge. Only present if the charge came from another Stripe
   * account. [See the Connect documentation](/docs/connect/destination-charges) for details.
   */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Transfer> sourceTransfer;

  /**
   * Extra information about a charge. This will appear on your customer's credit card statement. It
   * must contain at least one letter.
   */
  String statementDescriptor;

  /** The status of the payment is either `succeeded`, `pending`, or `failed`. */
  String status;

  /**
   * ID of the transfer to the `destination` account (only applicable if the charge was created
   * using the `destination` parameter).
   */
  @Getter(lombok.AccessLevel.NONE)
  @Setter(lombok.AccessLevel.NONE)
  ExpandableField<Transfer> transfer;

  TransferData transferData;

  /**
   * A string that identifies this transaction as part of a group. See the [Connect
   * documentation](/docs/connect/charges-transfers#grouping-transactions) for details.
   */
  String transferGroup;

  /** Get id of expandable `application` object. */
  public String getApplication() {
    return (this.application != null) ? this.application.getId() : null;
  }

  public void setApplication(String id) {
    this.application = ApiResource.setExpandableFieldId(id, this.application);
  }

  /** Get expanded `application`. */
  public Application getApplicationObject() {
    return (this.application != null) ? this.application.getExpanded() : null;
  }

  public void setApplicationObject(Application expandableObject) {
    this.application = new ExpandableField<Application>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `applicationFee` object. */
  public String getApplicationFee() {
    return (this.applicationFee != null) ? this.applicationFee.getId() : null;
  }

  public void setApplicationFee(String id) {
    this.applicationFee = ApiResource.setExpandableFieldId(id, this.applicationFee);
  }

  /** Get expanded `applicationFee`. */
  public ApplicationFee getApplicationFeeObject() {
    return (this.applicationFee != null) ? this.applicationFee.getExpanded() : null;
  }

  public void setApplicationFeeObject(ApplicationFee expandableObject) {
    this.applicationFee =
        new ExpandableField<ApplicationFee>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `balanceTransaction` object. */
  public String getBalanceTransaction() {
    return (this.balanceTransaction != null) ? this.balanceTransaction.getId() : null;
  }

  public void setBalanceTransaction(String id) {
    this.balanceTransaction = ApiResource.setExpandableFieldId(id, this.balanceTransaction);
  }

  /** Get expanded `balanceTransaction`. */
  public BalanceTransaction getBalanceTransactionObject() {
    return (this.balanceTransaction != null) ? this.balanceTransaction.getExpanded() : null;
  }

  public void setBalanceTransactionObject(BalanceTransaction expandableObject) {
    this.balanceTransaction =
        new ExpandableField<BalanceTransaction>(expandableObject.getId(), expandableObject);
  }

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

  /** Get id of expandable `destination` object. */
  public String getDestination() {
    return (this.destination != null) ? this.destination.getId() : null;
  }

  public void setDestination(String id) {
    this.destination = ApiResource.setExpandableFieldId(id, this.destination);
  }

  /** Get expanded `destination`. */
  public Account getDestinationObject() {
    return (this.destination != null) ? this.destination.getExpanded() : null;
  }

  public void setDestinationObject(Account expandableObject) {
    this.destination = new ExpandableField<Account>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `dispute` object. */
  public String getDispute() {
    return (this.dispute != null) ? this.dispute.getId() : null;
  }

  public void setDispute(String id) {
    this.dispute = ApiResource.setExpandableFieldId(id, this.dispute);
  }

  /** Get expanded `dispute`. */
  public Dispute getDisputeObject() {
    return (this.dispute != null) ? this.dispute.getExpanded() : null;
  }

  public void setDisputeObject(Dispute expandableObject) {
    this.dispute = new ExpandableField<Dispute>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `invoice` object. */
  public String getInvoice() {
    return (this.invoice != null) ? this.invoice.getId() : null;
  }

  public void setInvoice(String id) {
    this.invoice = ApiResource.setExpandableFieldId(id, this.invoice);
  }

  /** Get expanded `invoice`. */
  public Invoice getInvoiceObject() {
    return (this.invoice != null) ? this.invoice.getExpanded() : null;
  }

  public void setInvoiceObject(Invoice expandableObject) {
    this.invoice = new ExpandableField<Invoice>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `onBehalfOf` object. */
  public String getOnBehalfOf() {
    return (this.onBehalfOf != null) ? this.onBehalfOf.getId() : null;
  }

  public void setOnBehalfOf(String id) {
    this.onBehalfOf = ApiResource.setExpandableFieldId(id, this.onBehalfOf);
  }

  /** Get expanded `onBehalfOf`. */
  public Account getOnBehalfOfObject() {
    return (this.onBehalfOf != null) ? this.onBehalfOf.getExpanded() : null;
  }

  public void setOnBehalfOfObject(Account expandableObject) {
    this.onBehalfOf = new ExpandableField<Account>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `order` object. */
  public String getOrder() {
    return (this.order != null) ? this.order.getId() : null;
  }

  public void setOrder(String id) {
    this.order = ApiResource.setExpandableFieldId(id, this.order);
  }

  /** Get expanded `order`. */
  public Order getOrderObject() {
    return (this.order != null) ? this.order.getExpanded() : null;
  }

  public void setOrderObject(Order expandableObject) {
    this.order = new ExpandableField<Order>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `review` object. */
  public String getReview() {
    return (this.review != null) ? this.review.getId() : null;
  }

  public void setReview(String id) {
    this.review = ApiResource.setExpandableFieldId(id, this.review);
  }

  /** Get expanded `review`. */
  public Review getReviewObject() {
    return (this.review != null) ? this.review.getExpanded() : null;
  }

  public void setReviewObject(Review expandableObject) {
    this.review = new ExpandableField<Review>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `sourceTransfer` object. */
  public String getSourceTransfer() {
    return (this.sourceTransfer != null) ? this.sourceTransfer.getId() : null;
  }

  public void setSourceTransfer(String id) {
    this.sourceTransfer = ApiResource.setExpandableFieldId(id, this.sourceTransfer);
  }

  /** Get expanded `sourceTransfer`. */
  public Transfer getSourceTransferObject() {
    return (this.sourceTransfer != null) ? this.sourceTransfer.getExpanded() : null;
  }

  public void setSourceTransferObject(Transfer expandableObject) {
    this.sourceTransfer = new ExpandableField<Transfer>(expandableObject.getId(), expandableObject);
  }

  /** Get id of expandable `transfer` object. */
  public String getTransfer() {
    return (this.transfer != null) ? this.transfer.getId() : null;
  }

  public void setTransfer(String id) {
    this.transfer = ApiResource.setExpandableFieldId(id, this.transfer);
  }

  /** Get expanded `transfer`. */
  public Transfer getTransferObject() {
    return (this.transfer != null) ? this.transfer.getExpanded() : null;
  }

  public void setTransferObject(Transfer expandableObject) {
    this.transfer = new ExpandableField<Transfer>(expandableObject.getId(), expandableObject);
  }

  /**
   * Returns a list of charges you’ve previously created. The charges are returned in sorted order,
   * with the most recent charges appearing first.
   */
  public static ChargeCollection list(Map<String, Object> params) throws StripeException {
    return list(params, (RequestOptions) null);
  }

  /**
   * Returns a list of charges you’ve previously created. The charges are returned in sorted order,
   * with the most recent charges appearing first.
   */
  public static ChargeCollection list(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", Stripe.getApiBase(), "/v1/charges");
    return requestCollection(url, params, ChargeCollection.class, options);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won’t actually be
   * charged, although everything else will occur as if in live mode. (Stripe assumes that the
   * charge would have completed successfully).
   */
  public static Charge create(Map<String, Object> params) throws StripeException {
    return create(params, (RequestOptions) null);
  }

  /**
   * To charge a credit card or other payment source, you create a <code>Charge</code> object. If
   * your API key is in test mode, the supplied payment source (e.g., card) won’t actually be
   * charged, although everything else will occur as if in live mode. (Stripe assumes that the
   * charge would have completed successfully).
   */
  public static Charge create(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", Stripe.getApiBase(), "/v1/charges");
    return request(ApiResource.RequestMethod.POST, url, params, Charge.class, options);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique charge ID
   * that was returned from your previous request, and Stripe will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static Charge retrieve(String charge) throws StripeException {
    return retrieve(charge, (Map<String, Object>) null, (RequestOptions) null);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique charge ID
   * that was returned from your previous request, and Stripe will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static Charge retrieve(String charge, RequestOptions options) throws StripeException {
    return retrieve(charge, (Map<String, Object>) null, options);
  }

  /**
   * Retrieves the details of a charge that has previously been created. Supply the unique charge ID
   * that was returned from your previous request, and Stripe will return the corresponding charge
   * information. The same information is returned when creating or refunding the charge.
   */
  public static Charge retrieve(String charge, Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format("%s%s", Stripe.getApiBase(), String.format("/v1/charges/%s", charge));
    return request(ApiResource.RequestMethod.GET, url, params, Charge.class, options);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public Charge update(Map<String, Object> params) throws StripeException {
    return update(params, (RequestOptions) null);
  }

  /**
   * Updates the specified charge by setting the values of the parameters passed. Any parameters not
   * provided will be left unchanged.
   */
  public Charge update(Map<String, Object> params, RequestOptions options) throws StripeException {
    String url =
        String.format("%s%s", Stripe.getApiBase(), String.format("/v1/charges/%s", this.getId()));
    return request(ApiResource.RequestMethod.POST, url, params, Charge.class, options);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you <a href="#create_charge">created a charge</a> with the capture
   * option set to false.
   *
   * <p>Uncaptured payments expire exactly seven days after they are created. If they are not
   * captured by that point in time, they will be marked as refunded and will no longer be
   * capturable.
   */
  public Charge capture() throws StripeException {
    return capture((Map<String, Object>) null, (RequestOptions) null);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you <a href="#create_charge">created a charge</a> with the capture
   * option set to false.
   *
   * <p>Uncaptured payments expire exactly seven days after they are created. If they are not
   * captured by that point in time, they will be marked as refunded and will no longer be
   * capturable.
   */
  public Charge capture(RequestOptions options) throws StripeException {
    return capture((Map<String, Object>) null, options);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you <a href="#create_charge">created a charge</a> with the capture
   * option set to false.
   *
   * <p>Uncaptured payments expire exactly seven days after they are created. If they are not
   * captured by that point in time, they will be marked as refunded and will no longer be
   * capturable.
   */
  public Charge capture(Map<String, Object> params) throws StripeException {
    return capture(params, (RequestOptions) null);
  }

  /**
   * Capture the payment of an existing, uncaptured, charge. This is the second half of the two-step
   * payment flow, where first you <a href="#create_charge">created a charge</a> with the capture
   * option set to false.
   *
   * <p>Uncaptured payments expire exactly seven days after they are created. If they are not
   * captured by that point in time, they will be marked as refunded and will no longer be
   * capturable.
   */
  public Charge capture(Map<String, Object> params, RequestOptions options) throws StripeException {
    String url =
        String.format(
            "%s%s", Stripe.getApiBase(), String.format("/v1/charges/%s/capture", this.getId()));
    return request(ApiResource.RequestMethod.POST, url, params, Charge.class, options);
  }

  /**
   * When you create a new refund, you must specify a charge on which to create it.
   *
   * <p>Creating a new refund will refund a charge that has previously been created but not yet
   * refunded. Funds will be refunded to the credit or debit card that was originally charged.
   *
   * <p>You can optionally refund only part of a charge. You can do so multiple times, until the
   * entire charge has been refunded.
   *
   * <p>Once entirely refunded, a charge can’t be refunded again. This method will raise an error
   * when called on an already-refunded charge, or when trying to refund more money than is left on
   * a charge.
   */
  public Charge refund() throws StripeException {
    return refund((Map<String, Object>) null, (RequestOptions) null);
  }

  /**
   * When you create a new refund, you must specify a charge on which to create it.
   *
   * <p>Creating a new refund will refund a charge that has previously been created but not yet
   * refunded. Funds will be refunded to the credit or debit card that was originally charged.
   *
   * <p>You can optionally refund only part of a charge. You can do so multiple times, until the
   * entire charge has been refunded.
   *
   * <p>Once entirely refunded, a charge can’t be refunded again. This method will raise an error
   * when called on an already-refunded charge, or when trying to refund more money than is left on
   * a charge.
   */
  public Charge refund(RequestOptions options) throws StripeException {
    return refund((Map<String, Object>) null, options);
  }

  /**
   * When you create a new refund, you must specify a charge on which to create it.
   *
   * <p>Creating a new refund will refund a charge that has previously been created but not yet
   * refunded. Funds will be refunded to the credit or debit card that was originally charged.
   *
   * <p>You can optionally refund only part of a charge. You can do so multiple times, until the
   * entire charge has been refunded.
   *
   * <p>Once entirely refunded, a charge can’t be refunded again. This method will raise an error
   * when called on an already-refunded charge, or when trying to refund more money than is left on
   * a charge.
   */
  public Charge refund(Map<String, Object> params) throws StripeException {
    return refund(params, (RequestOptions) null);
  }

  /**
   * When you create a new refund, you must specify a charge on which to create it.
   *
   * <p>Creating a new refund will refund a charge that has previously been created but not yet
   * refunded. Funds will be refunded to the credit or debit card that was originally charged.
   *
   * <p>You can optionally refund only part of a charge. You can do so multiple times, until the
   * entire charge has been refunded.
   *
   * <p>Once entirely refunded, a charge can’t be refunded again. This method will raise an error
   * when called on an already-refunded charge, or when trying to refund more money than is left on
   * a charge.
   */
  public Charge refund(Map<String, Object> params, RequestOptions options) throws StripeException {
    String url =
        String.format(
            "%s%s", Stripe.getApiBase(), String.format("/v1/charges/%s/refund", this.getId()));
    return request(ApiResource.RequestMethod.POST, url, params, Charge.class, options);
  }

  /** Retrieve a dispute for a specified charge. */
  public Dispute retrieveDispute() throws StripeException {
    return retrieveDispute((Map<String, Object>) null, (RequestOptions) null);
  }

  /** Retrieve a dispute for a specified charge. */
  public Dispute retrieveDispute(Map<String, Object> params) throws StripeException {
    return retrieveDispute(params, (RequestOptions) null);
  }

  /** Retrieve a dispute for a specified charge. */
  public Dispute retrieveDispute(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url =
        String.format(
            "%s%s", Stripe.getApiBase(), String.format("/v1/charges/%s/dispute", this.getId()));
    return request(ApiResource.RequestMethod.GET, url, params, Dispute.class, options);
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class AlternateStatementDescriptors extends StripeObject {
    /** The Kana variation of the descriptor. */
    String kana;

    /** The Kanji variation of the descriptor. */
    String kanji;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class FraudDetails extends StripeObject {
    /** Assessments from Stripe. If set, the value is `fraudulent`. */
    String stripeReport;

    /** Assessments reported by you. If set, possible values of are `safe` and `fraudulent`. */
    String userReport;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class Outcome extends StripeObject {
    /**
     * Possible values are `approved_by_network`, `declined_by_network`, `not_sent_to_network`, and
     * `reversed_after_approval`. The value `reversed_after_approval` indicates the payment was
     * [blocked by Stripe](/docs/declines#blocked-payments) after bank authorization, and may
     * temporarily appear as "pending" on a cardholder's statement.
     */
    String networkStatus;

    /**
     * An enumerated value providing a more detailed explanation of the outcome's `type`. Charges
     * blocked by Radar's default block rule have the value `highest_risk_level`. Charges placed in
     * review by Radar's default review rule have the value `elevated_risk_level`. Charges
     * authorized, blocked, or placed in review by custom rules have the value `rule`. See
     * [understanding declines](/docs/declines) for more details.
     */
    String reason;

    /**
     * Stripe's evaluation of the riskiness of the payment. Possible values for evaluated payments
     * are `normal`, `elevated`, `highest`. For non-card payments, and card-based payments predating
     * the public assignment of risk levels, this field will have the value `not_assessed`. In the
     * event of an error in the evaluation, this field will have the value `unknown`.
     */
    String riskLevel;

    /**
     * Stripe's evaluation of the riskiness of the payment. Possible values for evaluated payments
     * are between 0 and 100. For non-card payments, card-based payments predating the public
     * assignment of risk scores, or in the event of an error during evaluation, this field will not
     * be present. This field is only available with Radar for Fraud Teams.
     */
    Long riskScore;

    /** The ID of the Radar rule that matched the payment, if applicable. */
    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    ExpandableField<Rule> rule;

    /**
     * A human-readable description of the outcome type and reason, designed for you (the recipient
     * of the payment), not your customer.
     */
    String sellerMessage;

    /**
     * Possible values are `authorized`, `manual_review`, `issuer_declined`, `blocked`, and
     * `invalid`. See [understanding declines](/docs/declines) and [Radar reviews](radar/review) for
     * details.
     */
    String type;

    /** Get id of expandable `rule` object. */
    public String getRule() {
      return (this.rule != null) ? this.rule.getId() : null;
    }

    public void setRule(String id) {
      this.rule = ApiResource.setExpandableFieldId(id, this.rule);
    }

    /** Get expanded `rule`. */
    public Rule getRuleObject() {
      return (this.rule != null) ? this.rule.getExpanded() : null;
    }

    public void setRuleObject(Rule expandableObject) {
      this.rule = new ExpandableField<Rule>(expandableObject.getId(), expandableObject);
    }
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class TransferData extends StripeObject {
    /**
     * The account (if any) the charge was made on behalf of, with an automatic transfer. [See the
     * Connect documentation](/docs/connect/destination-charges) for details.
     */
    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    ExpandableField<Account> destination;

    /** Get id of expandable `destination` object. */
    public String getDestination() {
      return (this.destination != null) ? this.destination.getId() : null;
    }

    public void setDestination(String id) {
      this.destination = ApiResource.setExpandableFieldId(id, this.destination);
    }

    /** Get expanded `destination`. */
    public Account getDestinationObject() {
      return (this.destination != null) ? this.destination.getExpanded() : null;
    }

    public void setDestinationObject(Account expandableObject) {
      this.destination = new ExpandableField<Account>(expandableObject.getId(), expandableObject);
    }
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class Level3 extends StripeObject {
    String customerReference;

    List<Level3LineItem> lineItems;

    String merchantReference;

    String shippingAddressZip;

    Long shippingAmount;

    String shippingFromZip;
  }

  @Getter
  @Setter
  @EqualsAndHashCode(callSuper = false)
  public static class Level3LineItem extends StripeObject {
    Long discountAmount;

    String productCode;

    String productDescription;

    Long quantity;

    Long taxAmount;

    Long unitCost;
  }
}
