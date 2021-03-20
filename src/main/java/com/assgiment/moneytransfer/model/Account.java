package com.assgiment.moneytransfer.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private UUID id;

	@Column(name = "account_number")
	private Long accountNumber; // Auto Generated String Value

	@Column(name = "account_status")
	private String accountStatus;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "account_balance")
	private Double accountBalance;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDateTime;

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToOne(cascade = CascadeType.ALL)
	private BankDetail bankDetail;

}
