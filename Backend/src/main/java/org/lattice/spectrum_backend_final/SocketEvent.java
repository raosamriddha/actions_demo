package org.lattice.spectrum_backend_final;

/**
 * @author RAHUL KUMAR MAURYA
 */

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.util.Logger;

public class SocketEvent {
    public static void events(final SocketIOServer socketServer) {
        socketServer.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                try {
                    if(!DeviceManager.getInstance().isSocketSet()){
                        Logger.debug(this, "SOCKET_EVENT : Socket connected. : Active threads : "+Thread.activeCount());
                        DeviceManager.getInstance().setSocketSet(true);
                        DeviceManager.getInstance().setSocketServer(socketServer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        socketServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                Logger.debug(this, "SOCKET_EVENT : Socket disconnected. : Active threads : "+Thread.activeCount());
            }
        });

    }

}
