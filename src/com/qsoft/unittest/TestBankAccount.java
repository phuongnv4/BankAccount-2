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

	// 1
	@Test
	public void testOpenAccount() {

		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		assertEquals(0, bAccountDto.getBalance(), 0.001);
	}

	// 2
	@Test
	public void testGetAccount() {

		ArgumentCaptor<BankAccountDTO> argumentDTO = ArgumentCaptor
				.forClass(BankAccountDTO.class);

		bAccount.openAccount("123456789");

		verify(bankAccountDAO).save(argumentDTO.capture());
		assertEquals(argumentDTO.getValue().getBalance(), 0.0, 0.01);
		assertEquals(argumentDTO.getValue().getAccountNumber(), "123456789");

	}

	// 3
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

	// 4
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

	// 5
	@Test
	public void testWithDraw() {
		ArgumentCaptor<BankAccountDTO> argumentDTO = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");

		// deposit
		bAccount.deposit(bAccountDto, 60, "phuongnv save money");
		verify(bankAccountDAO, times(2)).save(argumentDTO.capture());
		List<BankAccountDTO> savedAccountRecords = argumentDTO.getAllValues();
		assertEquals(savedAccountRecords.get(1).getBalance(), 60, 0.001);

		// withdraw
		bAccount.withdraw(bAccountDto, -50, "Phuongnv rut tien");
		verify(bankAccountDAO, times(3)).save(argumentDTO.capture());
		List<BankAccountDTO> withDraw = argumentDTO.getAllValues();
		assertEquals(withDraw.get(2).getBalance(), 10, 0.001);
	}

	// 7
	@Test
	public void testGetTransactionsOccurred() {
		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		//
		bAccount.getTransactionsOccurred(bAccountDto.getAccountNumber());
		verify(bankAccountDAO).getListTransactions(
				bAccountDto.getAccountNumber());
	}

	// 8
	@Test
	public void testGetTransactionsOccurred2() {
		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");

		bAccount.getTransactionsOccurred(bAccountDto.getAccountNumber(), 1L, 5L);
		ArgumentCaptor<String> accountNumber = ArgumentCaptor
				.forClass(String.class);
		ArgumentCaptor<Long> argumentStartTime = ArgumentCaptor
				.forClass(Long.class);
		ArgumentCaptor<Long> argumentStopTime = ArgumentCaptor
				.forClass(Long.class);

		verify(bankAccountDAO).getListTransactions(accountNumber.capture(),
				argumentStartTime.capture(), argumentStopTime.capture());

		assertEquals(1L, argumentStartTime.getValue().longValue());
		assertEquals(5L, argumentStopTime.getValue().longValue());
	}

	// 9
	@Test
	public void testGetNTransaction() {
		ArgumentCaptor<BankAccountDTO> argumentDTO = ArgumentCaptor
				.forClass(BankAccountDTO.class);
		ArgumentCaptor<Integer> n = ArgumentCaptor.forClass(Integer.class);
		BankAccountDTO bAccountDto = bAccount.openAccount("123456789");
		bAccount.getNTransactions(bAccountDto, 20);

		verify(bankAccountDAO).getNTransactions(argumentDTO.capture(),
				n.capture());
		assertEquals(20, n.getValue().intValue());
	}
}
