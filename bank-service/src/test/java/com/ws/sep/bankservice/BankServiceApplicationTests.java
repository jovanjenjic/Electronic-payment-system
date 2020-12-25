package com.ws.sep.bankservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import utils.EncryptionDecryption;

@SpringBootTest
class BankServiceApplicationTests
{

	@Test
	void contextLoads()
	{

	}


	@Test
	void testEncryption()
	{
		String text = "someTextToEncrypt";

		String encryptString = EncryptionDecryption.encryptString( text );
		String decryptString = EncryptionDecryption.decryptString( encryptString );

		Assertions.assertEquals( text, decryptString );

	}

}
