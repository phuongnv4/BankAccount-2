package com.qsoft.unittest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

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
		assertEquals(0, bAccountDto.getBalance());
	}

	@Test
	public void testGetAccount() {

		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		assertEquals("123456789", bAccountDto.getAccountNumber());
		verify(bankAccountDAO).save(bAccountDto);

	}
}
