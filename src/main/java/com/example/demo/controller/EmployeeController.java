package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService empservice;
	
	//display list of employee
	@GetMapping("/")
	public String viewHomePage(Model md)
	{
		//md.addAttribute("listEmployees",empservice.getAllEmployees());
		//return "index";
		return findPaginated(1, "firstName", "asc", md);
	}
	//---------------Show-----------------
	@GetMapping("/showNewEmployeeForm")
	public String showNewEmployeeForm(Model md)
	{
		Employee employee = new Employee();
		md.addAttribute("employee", employee);
		return "new_employee";
	}
	//-----------------Save---------------
	@PostMapping("/saveEmployee")
	public String saveEmployee(@ModelAttribute("employee") Employee employee)
	{
		empservice.saveEmployee(employee);
		return "redirect:/";		
	}
	//---------------Update-----------------
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") long id, Model md)
	{
		Employee employee = empservice.getEmployeById(id);
		md.addAttribute("employee", employee);		
		return "update_employee";		
	}
	//----------------------Delete----------
	@GetMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable(value = "id") long id)
	{
		this.empservice.deleteEmployeeById(id);
		return "redirect:/";	
	}
	//----------paging-------------
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model md)
	{
		int pageSize = 5;
		Page<Employee> page = empservice.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Employee> listEmployees = page.getContent();
		
		md.addAttribute("currentPage", pageNo);
		md.addAttribute("totalPages", page.getTotalPages());
		md.addAttribute("totalItems", page.getTotalElements());
		
		md.addAttribute("sortField", sortField);
		md.addAttribute("sortDir", sortDir);
		md.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		md.addAttribute("listEmployees", listEmployees);
		return "index";
		
	}

}
