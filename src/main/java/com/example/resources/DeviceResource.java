package com.example.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.example.model.DeviceRequest;
import com.example.dao.DeviceDAO;
import com.hazelcast.core.HazelcastInstance;

@Path("/device")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {
    private final HazelcastInstance hazelcastInstance;
    private final DeviceDAO deviceDAO;

    public DeviceResource(HazelcastInstance hazelcastInstance, DeviceDAO deviceDAO) {
        this.hazelcastInstance = hazelcastInstance;
        this.deviceDAO = deviceDAO;
    }

    @PATCH
    @Path("/{device_id}")
    public Response patchDevice(@PathParam("device_id") String deviceId, DeviceRequest request) {
        ConcurrentMap<String, DeviceRequest> requestMap = hazelcastInstance.getMap("device-requests");

        // Check if there is a waiting request for the same customer
        List<DeviceRequest> waitingRequests = deviceDAO.getWaitingRequests(request.getCustomerId());
        if (!waitingRequests.isEmpty()) {
            String trackerId = waitingRequests.get(0).getTrackerId();
            return Response.status(Response.Status.ACCEPTED).header("Tracker-ID", trackerId).build();
        }

        // Generate new tracker ID
        String trackerId = "TR" + System.currentTimeMillis();
        request.setTrackerId(trackerId);
        request.setStatus("Waiting");
        request.setCreatedTimestamp(Instant.now());

        // Add to Hazelcast map
        requestMap.put(trackerId, request);

        return Response.status(Response.Status.ACCEPTED).header("Tracker-ID", trackerId).build();
    }
}