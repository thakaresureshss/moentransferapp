package com.assgiment.moneytransfer.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "customer_number")
	private Long customerNumber;

	@Column(name = "status")
	private String status;

	@Column(name = "birth_date")
	@Temporal(TemporalType.DATE)
	private Date dob;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Address> customerAddresses;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Contact contactDetails;

	@Temporal(TemporalType.TIME)
	private Date createDateTime;

	@Temporal(TemporalType.TIME)
	private Date updateDateTime;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Account> accounts;

}
