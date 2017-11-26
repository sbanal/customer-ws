package com.github.sbanal.web.dto;

/**
 * State enum represents define the different states of Australia.
 *
 * @author Stephen Lou Banal
 * @version %I%, %G%
 */
public enum AddressState {
  ACT("Australian Capital Territory"),
  NSW("New South Wales"),
  NT("Northern Territory"),
  QLD("Queensland"),
  SA("South Australia"),
  TAS("Tasmania"),
  VIC("Victoria"),
  WA("Western Australia"),
  OTH("Others");

  private final String state;

  AddressState(String state) {
    this.state = state;
  }

  public String getName() {
    return this.state;
  }

}
