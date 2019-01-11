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
public class AccountLink extends ApiResource {
  /** Time at which the object was created. Measured in seconds since the Unix epoch. */
  Long created;

  /** The timestamp at which this account link will expire. */
  Long expiresAt;

  /** String representing the object's type. Objects of the same type share the same value. */
  String object;

  /** The URL for the account link. */
  String url;

  /**
   * Creates an AccountLink object that returns a Stripe URL that the user can redirect their user
   * to in order to take them through the Hosted Verification flow.
   */
  public static AccountLink create(Map<String, Object> params) throws StripeException {
    return create(params, (RequestOptions) null);
  }

  /**
   * Creates an AccountLink object that returns a Stripe URL that the user can redirect their user
   * to in order to take them through the Hosted Verification flow.
   */
  public static AccountLink create(Map<String, Object> params, RequestOptions options)
      throws StripeException {
    String url = String.format("%s%s", Stripe.getApiBase(), "/v1/account_links");
    return request(ApiResource.RequestMethod.POST, url, params, AccountLink.class, options);
  }
}
