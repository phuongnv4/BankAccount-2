package com.qsoft.unittest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.qsoft.BankAccount;
import com.qsoft.BankAccountDAO;
import com.qsoft.BankAccountDTO;

public class TestBankAccount {
	BankAccount bAccount;
	BankAccountDAO bankAccountDAO;

	@Before
	public void setUp() {
		bAccount = new BankAccount();
		bankAccountDAO = mock(BankAccountDAO.class);
		bAccount.setDao(bankAccountDAO);
	}

	@Test
	public void testOpenAccount() {

		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		assertEquals(0, bAccountDto.getBalance(), 0.001);
	}

	@Test
	public void testGetAccount() {

		ArgumentCaptor<BankAccountDTO> argumentDTO = ArgumentCaptor
				.forClass(BankAccountDTO.class);

		bAccount.openAccount("123456789");

		verify(bankAccountDAO).save(argumentDTO.capture());
		assertEquals(argumentDTO.getValue().getBalance(), 0.0, 0.01);
		assertEquals(argumentDTO.getValue().getAccountNumber(), "123456789");

	}

	@Test
	public void testDeposit() {
		ArgumentCaptor<BankAccountDTO> argumentDTO = ArgumentCaptor
				.forClass(BankAccountDTO.class);

		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		bAccountDto.setBalance(1);
		bAccount.deposit(bAccountDto, 10, "phuongnv save money");
		verify(bankAccountDAO, times(2)).save(argumentDTO.capture());

		List<BankAccountDTO> savedAccountRecords = argumentDTO.getAllValues();

		assertEquals(savedAccountRecords.get(1).getBalance(), 11, 0.001);

	}

	@Test
	public void testDepositWithTimeStamp() {
		ArgumentCaptor<BankAccountDTO> argumentDTO = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		ArgumentCaptor<Long> argumentTimeStamp = ArgumentCaptor
				.forClass(Long.class);

		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		bAccount.deposit(bAccountDto, 10, "phuongnv save money", 1L);
		verify(bankAccountDAO).save(argumentDTO.capture(),
				argumentTimeStamp.capture());

		assertEquals(1L, argumentTimeStamp.getValue().longValue());
	}

}
