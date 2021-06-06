package com.example.demo.systemUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.accesslog.AccessLogService;
import com.opencsv.exceptions.CsvException;

@Controller
public class SystemUserController {

	@Autowired
	private SystemUserService systemUserService;

	@Autowired
	private SystemUserRepository systemUserRepository;

	@Autowired
	private SystemUserValidator systemUserValidator;

	@Autowired
	private AccessLogService accessLogService;

    @Autowired
    HttpSession session;

	@InitBinder("systemUserForm")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(systemUserValidator);
	}

	@RequestMapping(value = "/systemUser/detail")
	private String detail(@RequestParam(name = "id", required = false) Long id, @ModelAttribute("systemUserForm") SystemUserForm systemUserForm, HttpSession session) {

		if(id == null) {
			// 新規登録
		}else {
			// 更新
			var systemUser = systemUserRepository.findById(id).get();

			BeanUtils.copyProperties(systemUser, systemUserForm);
			systemUserForm.setId(id);
		}

		session.setAttribute("systemUserForm", systemUserForm);

		return "/systemUser/detail";
	}

	@RequestMapping(value = "/systemUser/edit")
	private String edit(@RequestParam(name = "id", required = false) Long id, @ModelAttribute("systemUserForm") SystemUserForm systemUserForm, HttpSession session) {

		if(id == null) {
			// 新規登録
		}else {
			// 更新
			var systemUser = systemUserRepository.findById(id).get();

			BeanUtils.copyProperties(systemUser, systemUserForm);
			systemUserForm.setId(id);
		}

		session.setAttribute("systemUserForm", systemUserForm);

		return "/systemUser/edit";
	}

	@RequestMapping("/systemUser/editCheck")
	public String editCheck(@Validated @ModelAttribute SystemUserForm systemUserForm, BindingResult result, HttpSession session) {

		session.setAttribute("systemUserForm", systemUserForm);

		if(result.hasErrors()) {
			return "/systemUser/edit";
		}

		return "/systemUser/editCheck";
	}

	@PostMapping("/systemUser/finish")
	public String finish(HttpSession session, @ModelAttribute("systemUserForm") SystemUserForm systemUserForm) {

		var systemUser = new SystemUser();

		if(systemUserForm.getId() == null) {

			systemUserForm.setId((long) 0);
		}

		if(systemUserForm.getRole().equals("")) {

			systemUserForm.setRole("ROLE_USER");
		}

		BeanUtils.copyProperties(systemUserForm, systemUser);

		this.systemUserService.save(systemUser);

		accessLogService.save(1, "【システムユーザー】更新", "成功");

		return "/systemUser/finish";
	}

	@RequestMapping(value = "/systemUser/pagenate")
	public String pagenate(Model model, @PageableDefault(page = 0, size = 5) Pageable pageable) {

		SystemUserListForm systemUserListForm = (SystemUserListForm)session.getAttribute("systemUserListForm");

		return this.list(model, systemUserListForm, pageable);
	}

	@RequestMapping(value = "/systemUser/list")
	public String list(Model model, @ModelAttribute("systemUserListForm") SystemUserListForm systemUserListForm, @PageableDefault(page = 0, size = 5) Pageable pageable) {

		session.setAttribute("systemUserListForm", systemUserListForm);

		Page<SystemUser> list = systemUserService.findUsers(systemUserListForm, pageable);

		model.addAttribute("list", list.getContent());
        model.addAttribute("systemUserListForm",systemUserListForm);
        model.addAttribute("page",PagenationHelper.createPagenation(list));

        return "/systemUser/list";
	}

	@RequestMapping("/systemUser/delete")
	public String delete(@RequestParam(name = "id", required = false) Long id, Model model, @ModelAttribute("systemUserListForm") SystemUserListForm systemUserListForm) {

		this.systemUserRepository.deleteById(id);

		return this.list(model, systemUserListForm);
	}

	@RequestMapping("/systemUser/returnEdit")
	public String returnEdit(Model model, HttpSession session) {

		var sessionEditForm = (SystemUserForm) session.getAttribute("systemUserForm");

		if(sessionEditForm == null) {

			return "redirect:/systemUser/edit";
		}

		model.addAttribute("systemUserForm", sessionEditForm);

		return "/systemUser/edit";
	}

	@RequestMapping("/systemUser/returnDetail")
	public String returnDetail(Model model, HttpSession session) {

		var sessionEditForm = (SystemUserForm) session.getAttribute("systemUserForm");

		if(sessionEditForm == null) {

			return "redirect:/systemUser/detail";
		}

		model.addAttribute("systemUserForm", sessionEditForm);

		return "/systemUser/detail";
	}

	@RequestMapping("/systemUser/csvImport")
	public String csvImport(@RequestParam("csvFile") MultipartFile multipartFile, Model model, @ModelAttribute("systemUserListForm") SystemUserListForm systemUserListForm) throws IOException, CsvException {

		String line = null;
		InputStream stream = multipartFile.getInputStream();
		InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader buf= new BufferedReader(reader);

        while((line = buf.readLine()) != null) {

        	SystemUser systemUser = new SystemUser();

        	String[] splitList = line.split(",");

        	for(int i = 0; i < splitList.length; i++) {

        		systemUser.setName(splitList[i]);
        		systemUser.setAge(Integer.parseInt(splitList[++i]));
        		systemUser.setLoginid(splitList[++i]);
        		systemUser.setPassword(splitList[++i]);
        		systemUser.setRole(splitList[++i]);

        		this.systemUserService.save(systemUser);
        	}
        }

		return this.list(model, systemUserListForm);
	}

}
