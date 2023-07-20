package com.example.waggle.controller;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.dto.member.MemberSimpleDto;
import com.example.waggle.service.board.StoryService;
import com.example.waggle.service.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    public final MemberService memberService;
    public final StoryService storyService;

    /**
     * view
     */

    @GetMapping("/{username}")
    public String memberStory(@PathVariable String username,
                              Model model) {
        List<StorySimpleDto> allStoryByMember = storyService.findAllStoryByMember(username);
        model.addAttribute("simpleStories", allStoryByMember);
        return "story/memberStory";
    }

    @GetMapping("/{username}/{boardId}")
    public String story(@PathVariable Long boardId, Model model) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story", storyByBoardId);
        return "story/story";
    }

    /**
     * write
     */
    @GetMapping("/write")
    public String writeStoryForm(Model model) {
        String username = SecurityUtil.getCurrentUsername();
        MemberSimpleDto memberSimpleDto = memberService.findMemberSimpleDto(username);
        StoryDto storyDto = new StoryDto(memberSimpleDto.getUsername(), memberSimpleDto.getProfileImg());
        model.addAttribute("storyDto", storyDto);
        return "story/writeStory";
    }

    @PostMapping("/write")
    public String writeStory(@ModelAttribute StoryDto storyDto) {
        StoryDto savedStoryDto = storyService.saveStory(storyDto);
        return "redirect:/story/" + savedStoryDto.getUsername() + "/" + savedStoryDto.getId();
    }

    /**
     * edit
     */
    @GetMapping("/edit/{boardId}")
    public String editStoryForm(Model model, @PathVariable Long boardId) {
        StoryDto storyByBoardId = storyService.findStoryByBoardId(boardId);
        model.addAttribute("story", storyByBoardId);
        return "story/editStory";
    }

    @PostMapping("/edit/{boardId}")
    public String editStory(@ModelAttribute StoryDto storyDto) {
        storyService.changeStory(storyDto);
        String username = storyDto.getUsername();
        Long boardId = storyDto.getId();
        return "redirect:/story/" + username + "/" + boardId;
    }

    /**
     * remove
     */
}
