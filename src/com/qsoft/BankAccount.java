package com.qsoft;

public class BankAccount {
	private BankAccountDAO bankAccountDAO;

	public BankAccountDTO openAccount(String accountNumber) {
		BankAccountDTO bAccountDto = new BankAccountDTO();
		bAccountDto.setAccountNumber(accountNumber);
		bankAccountDAO.save(bAccountDto);
		return bAccountDto;
	}

	public void setDao(BankAccountDAO bankAccountDAO) {

		this.bankAccountDAO = bankAccountDAO;
	}

	public void deposit(BankAccountDTO bAccountDto, int amount, String descripton) {
		bAccountDto.setBalance(amount+bAccountDto.getBalance());
		bankAccountDAO.save(bAccountDto);
	}

}
