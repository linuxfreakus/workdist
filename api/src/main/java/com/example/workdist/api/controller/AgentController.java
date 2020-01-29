package com.example.workdist.api.controller;

import com.example.workdist.api.errors.UnknownPriorityException;
import com.example.workdist.api.errors.UnknownSkillNameException;
import com.example.workdist.api.errors.WorkItemAssignmentException;
import com.example.workdist.api.message.AssignWorkItemRequestMsg;
import com.example.workdist.common.enums.Priority;
import com.example.workdist.common.model.Agent;
import com.example.workdist.common.model.WorkItem;
import com.example.workdist.common.service.WorkAssignmentService;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    WorkAssignmentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<Agent> showAllAgents() {
        return service.getAllAgentsWithInProgressWorkItems();
    }

}
