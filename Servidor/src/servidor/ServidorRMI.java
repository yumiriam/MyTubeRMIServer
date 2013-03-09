package servidor;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import teste.InterfaceRMI;

public class ServidorRMI {

	private static final int PORT = 1099;
	private static Registry registry;

	public static void startRegistry() throws RemoteException {
		// create in server registry
		//registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
		registry = LocateRegistry.getRegistry(PORT);
	}
	
	public static void registerObject(String name, Remote remoteObj)
			throws RemoteException, AlreadyBoundException {
		registry.bind(name, remoteObj);
		System.out.println("Registered: " + name + " -> " +
		remoteObj.getClass().getName() + "[" + remoteObj + "]");
	}
			 
	public static void main(String[] args) throws Exception {
		startRegistry();
		registerObject("InterfaceRMI", new InterfaceRMIImpl());
	}
	
	/*public static void main(String[] args) {
        try {
            ServidorRMI obj = new ServidorRMI();
            InterfaceRMI stub = (InterfaceRMI) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("InterfaceRMI", stub);

            System.err.println("Servidor pronto");
        } catch (Exception e) {
            System.err.println("Erro de servidor: " + e.toString());
            e.printStackTrace();
        }
	}*/
}
