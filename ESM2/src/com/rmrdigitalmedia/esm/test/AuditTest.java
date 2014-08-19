package com.rmrdigitalmedia.esm.test;

import com.rmrdigitalmedia.esm.AppData;
import com.rmrdigitalmedia.esm.EsmApplication;
import com.rmrdigitalmedia.esm.controllers.AuditController;
import com.rmrdigitalmedia.esm.controllers.AuditControllerORM;
import com.rmrdigitalmedia.esm.controllers.FilesystemController;

public class AuditTest {

	public static void main(String[] args) {
		
		FilesystemController.createLogDir();

		System.out.println("Using ORM");
		EsmApplication.appData = new AppData();
		System.out.println(EsmApplication.appData.dump());
		AuditControllerORM.init();
		System.out.println(EsmApplication.appData.dump());

		System.out.println("\n\nUsing java.sql");	
		EsmApplication.appData = new AppData();
		System.out.println(EsmApplication.appData.dump());
		AuditController.init();	
		System.out.println(EsmApplication.appData.dump());

	}
	

}
