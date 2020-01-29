package com.example.workdist.api.controller;

import com.example.workdist.api.errors.*;
import com.example.workdist.api.message.AssignWorkItemRequestMsg;
import com.example.workdist.api.message.ResponseMsg;
import com.example.workdist.common.enums.Priority;
import com.example.workdist.common.model.Agent;
import com.example.workdist.common.model.WorkItem;
import com.example.workdist.common.service.WorkAssignmentService;
import com.google.common.collect.ImmutableSet;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Set;
import java.util.function.Supplier;

@RestController
@RequestMapping("/work-items")
public class WorkItemController {

    @Autowired
    WorkAssignmentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<WorkItem> showInProgressWorkItems() {
        return service.getInProgressWorkItems();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkItem assignWorkItem(@RequestBody final AssignWorkItemRequestMsg req)
            throws UnknownPriorityException, UnknownSkillNameException, WorkItemAssignmentException {

        Priority priority;
        try {
            priority = Priority.valueOf(req.getPriorityName().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new UnknownPriorityException(String.format("unknown priority: %s", req.getPriorityName()));
        }

        final WorkItem item = WorkItem.builder()
                .title(req.getTitle())
                .priority(priority)
                .requiredSkills(req.getRequiredSkillNames().stream()
                        .map(service::tryGetSkillByName)
                        .map(opt -> opt.orElseThrow(() -> new UnknownSkillNameException("unknown skill name(s)")))
                        .collect(ImmutableSet.toImmutableSet()))
                .build();

        return service.tryAssignWorkItem(item)
                .orElseThrow(() -> new WorkItemAssignmentException("could not assign work item to an agent"));
    }

    @PutMapping("/set-completed/{workItemId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkItem setWorkItemCompleted(@PathVariable Integer workItemId) throws WorkItemAlreadyCompletedException, WorkItemNotFoundException {
        try {
            return service.setWorkItemCompleted(workItemId);
        } catch (InvalidParameterException ipe) {
            throw new WorkItemAlreadyCompletedException(ipe.getMessage());
        } catch (NotFoundException nfe) {
            throw new WorkItemNotFoundException(nfe.getMessage());
        }
    }

}
