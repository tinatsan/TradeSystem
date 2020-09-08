package requests;

import accounts.users.User;
import accounts.users.UserManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all TradeRequests. Changes values in TradeRequest by accessing getters/setters. Should only be instantiated once.
 */
public class TradeRequestManager implements Serializable {
    private List<TradeRequest> pending;
    private List<TradeRequest> denied;
    private List<TradeRequest> confirmed;

    /**
     * Constructs an instance of TradeRequestManager. Does not require an arguments for instantiation, however, this constructor
     * will initialize three list attributes: pending, denied and confirmed.
     */
    public TradeRequestManager() {
        this.pending = new ArrayList<TradeRequest>();
        this.denied = new ArrayList<TradeRequest>();
        this.confirmed = new ArrayList<TradeRequest>();
    }

    /**
     * Adds an instance of TradeRequest to one of three attributes in TradeRequestManager: pending, confirmed or cancelled, depending on
     * the User receiving the Trade Request's response. Also, adds to User's pendingRequests if request is not yet acted upon by receiving User.
     *
     * @param userManager The instance of UserManager, to access addToPendingRequest.
     * @param request     The given instance of TradeRequest.
     */
    public void receiveTradeRequest(UserManager userManager, TradeRequest request) {
        User user1;
        User user2;
        if (request instanceof TypeOneRequest) {
            user1 = userManager.getUser(((TypeOneRequest) request).getFirstUser());
            user2 = userManager.getUser(((TypeOneRequest) request).getSecondUser());
            userManager.addToOutboundRequests(user1, request);
            userManager.addToWeeklyRequestLimit(user1, request);
            userManager.addToPendingRequests(user2, request);
        } else if (request instanceof TypeTwoRequest) {
            user1 = userManager.getUser(((TypeTwoRequest) request).getFirstUser());
            user2 = userManager.getUser(((TypeTwoRequest) request).getSecondUser());
            userManager.addToOutboundRequests(user1, request);
            userManager.addToWeeklyRequestLimit(user1, request);
            userManager.addToPendingRequests(user2, request);
        } else {
            user1 = userManager.getUser(((TypeThreeRequest) request).getFirstUser());
            user2 = userManager.getUser(((TypeThreeRequest) request).getSecondUser());
            User user3 = userManager.getUser(((TypeThreeRequest) request).getThirdUser());
            userManager.addToOutboundRequests(user2, request);
            userManager.addToWeeklyRequestLimit(user2, request);
            userManager.addToPendingRequests(user1, request);
            userManager.addToPendingRequests(user3, request);
        }
        this.pending.add(request);
    }

    /**
     * Updates attribute list(s) (pending/denied/confirmed) depending on the wishes of the User receiving the TradeRequest.
     *
     * @param userManager The instance of userManager, required for accessing handleConfirmedRequest.
     * @param request     The given TradeRequest that the receiving User intends to act upon.
     * @param status      The status the system intends to give to this TradeRequest.
     */
    public void updateRequestStatus(UserManager userManager, TradeRequest request, int status) {
        if (request.getStatus() == 0) {
            this.pending.remove(request);
        } else if (request.getStatus() == 1) {
            this.confirmed.remove(request);
        } else {
            this.denied.remove(request);
        }
        if (status == 0) {
            this.pending.add(request);
        } else if (status == 1) {
            this.confirmed.add(request);
            this.handleRequestResponse(userManager, request);
        } else {
            this.denied.add(request);
            this.handleRequestResponse(userManager, request);
        }
        request.setStatus(status);
    }

    /**
     * Accepts the TradeRequest within a User's pendingList, creates an instance of Transaction.
     *
     * @param userManager The instance of UserManager.
     * @param request     The status the system intends to give to this TradeRequest.
     */
    public void handleRequestResponse(UserManager userManager, TradeRequest request) {
        User user1;
        User user2;
        if (request instanceof TypeOneRequest) {
            user1 = userManager.getUser(((TypeOneRequest) request).getFirstUser());
            user2 = userManager.getUser(((TypeOneRequest) request).getSecondUser());
            userManager.removeFromOutboundRequests(user1, request);
            userManager.removeFromPendingRequests(user2, request);
        } else if (request instanceof TypeTwoRequest) {
            user1 = userManager.getUser(((TypeTwoRequest) request).getFirstUser());
            user2 = userManager.getUser(((TypeTwoRequest) request).getSecondUser());
            userManager.removeFromOutboundRequests(user1, request);
            userManager.removeFromPendingRequests(user2, request);
        } else {
            user1 = userManager.getUser(((TypeThreeRequest) request).getFirstUser());
            user2 = userManager.getUser(((TypeThreeRequest) request).getSecondUser());
            User user3 = userManager.getUser(((TypeThreeRequest) request).getThirdUser());
            userManager.removeFromOutboundRequests(user2, request);
            if (((TypeThreeRequest) request).getFirstApproved().getName().equals(user1.getName())) {
                userManager.removeFromApprovedThreeWay(user1, (TypeThreeRequest) request);
                userManager.removeFromPendingRequests(user3, request);
            } else if (((TypeThreeRequest) request).getFirstApproved().getName().equals(user3.getName())) {
                userManager.removeFromApprovedThreeWay(user3, (TypeThreeRequest) request);
                userManager.removeFromPendingRequests(user1, request);
            }
        }
    }

    /**
     * Handles when one of the three User's in the process of a ThreeWayRequest has approved the request.
     *
     * @param userManager The instance of UserManager
     * @param request     The given ThreeWayRequest
     * @param user        The User that approved the ThreeWay.
     */
    public void handleSingleApproved(UserManager userManager, TypeThreeRequest request, User user) {
        if (user.getName().equals(request.getFirstUser().getName())) {
            User user1 = userManager.getUser(user);
            userManager.removeFromPendingRequests(user1, request);
            userManager.addToApprovedThreeWay(user1, request);
        } else if (user.getName().equals(request.getThirdUser().getName())) {
            User user3 = userManager.getUser(user);
            userManager.removeFromPendingRequests(user3, request);
            userManager.addToApprovedThreeWay(user3, request);
        }
    }
}