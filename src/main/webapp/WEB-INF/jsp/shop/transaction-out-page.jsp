<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%><!DOCTYPE html>

		<div class="content">
			<h2>Purchasing</h2>

			<table style="layout: fixed" class="table">
				<tr>
					<td>
						<div class="form">
							<p>Stock ID</p>
								<input  type="number" class="form-control"
									id="stock-id"  required="required" />
									<button id="search-stock" onclick ="stockInfo()">OK</button>
							<p>Or Put ProductName</p>
							<input id="input-product" type="text" onkeyup="loadPrductList()"
								class="form-control" /> <br /> <select style="width: 300px"
								id="product-dropdown" class="form-control" multiple="multiple">
							</select>
							<hr>
							<p>Product Detail</p>
							<div class="panel">
								<p>
									Unit :<span id="unit-name"></span>
								</p>
								
								<p>Stock</p>
								<input disabled="disabled" type="number" class="form-control"
									id="stock-quantity" required="required" />
								<p>Product Quantity</p>
								<input type="number" class="form-control" id="product-quantity"
									required="required" />
								<p>Price @Unit</p>
								<input disabled="disabled" type="number" class="form-control"
									id="product-price" required="required" />
								<p>Expiry Date</p>
								<input disabled="disabled" type="date" class="form-control"
									id="product-exp-date" />
								<p></p>
								<button class="btn btn-submit" id="add-product" onclick="addToChart()">Add</button>
							</div>
						</div>
					</td>
					<td>
						<div class="form">
							<p>Customer Name</p>
							<input id="input-customer" type="text"
								onkeyup="loadCustomerList()" class="form-control" /> <br /> <select
								style="width: 200px" id="customer-dropdown" class="form-control"
								multiple="multiple">
							</select>
							<hr>
							<p>Customer Detail</p>
							<div class="panel">
								<h3 id="customer-name"></h3>
								<p id="customer-address"></p>
								<p id="customer-contact"></p>
							</div>
						</div>
					</td>
				</tr>
				<tr>
				</tr>
			</table>
			<div>
				<button class="btn btn-submit"  id="btn-send" onclick="send()">Submit Transaction</button>
			</div>
			<table class="table">
				<thead>
					<tr>
						<th>No</th>
						<th>Flow ID</th>
						<th>Product Name</th>
						<th>Expiry Date</th>
						<th>Quantity</th>
						<th>Price @Item</th>
						<th>Reff Stock ID</th>
						<th>Option</th>
					</tr>
					<tr>
						<th> </th>
						<th> </th>
						<th> </th>
						<th> </th>
						<th> </th>
						<th>Total:<span id="total-price"></span> </th>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tbody id="product-flows">

				</tbody>
			</table>
		</div>

	<script type="text/javascript">
		var productFlows = new Array();
		var currentProductFlow;
		var currentProduct;
		var currentCustomer;
		var inputProductField = document.getElementById("input-product");
		var stockIdField = document.getElementById("stock-id");
		var totalPriceLabel = document.getElementById("total-price");
		var productListDropDown = document.getElementById("product-dropdown");
		var productFlowTable = document.getElementById("product-flows");

		var inputCustomerField = document.getElementById("input-customer");
		var customerListDropDown = document.getElementById("customer-dropdown");
		function send() {
			if(!confirm("Are You Ready To Submit Transaction?"))
				return;
			var requestObject = {
				"customer" : currentCustomer,
				"productFlows" : productFlows
			}
			postReq("<spring:url value="/api/transaction/purchase" />",
					requestObject, function(xhr) {
						var response = (xhr.data);
						var code = response.code;
						if (code == "00") {
							alert("transaction success")
							productFlows = [];
							populateProductFlow(productFlows);
						} else {
							alert("transaction failed");
						}
					});
		}

		function loadCustomerList() {
			customerListDropDown.innerHTML = "";
			var requestObject = {
				"entity" : "customer",
				"filter" : {
					"page" : 0,
					"limit" : 10
				}
			};
			requestObject.filter.fieldsFilter = {};
			requestObject.filter.fieldsFilter["name"] = inputCustomerField.value;

			loadEntityList(
					"<spring:url value="/api/entity/get" />",
					requestObject,
					function(entities) {
						for (let i = 0; i < entities.length; i++) {
							let entity = entities[i];
							let option = document.createElement("option");
							option.value = entity["id"];
							option.innerHTML = entity["name"];
							option.onclick = function() {
								inputCustomerField.value = option.innerHTML;
								document.getElementById("customer-name").innerHTML = entity.name;
								/* document.getElementById("customer-address").innerHTML = entity.address;
								document.getElementById("customer-contact").innerHTML = entity.contact; */
								currentCustomer = entity;
							}
							customerListDropDown.append(option);
						}
					});
		}

		function loadPrductList() {
			productListDropDown.innerHTML = "";
			var requestObject = {
				"product" : {
					"name" : inputProductField.value
				}
			};

			loadEntityList("<spring:url value="/api/transaction/stocks" />",
					requestObject, function(entities) {
						for (let i = 0; i < entities.length; i++) {
							let flowEntity = entities[i];
							let entity = flowEntity.product;
							let option = document.createElement("option");
							option.value = entity["id"];
							option.innerHTML = flowEntity.id + "-"
									+ entity["name"];
							option.onclick = function() {
								setCurrentProduct(flowEntity, true);
							}
							productListDropDown.append(option);
						}
					});
		}

		/***COMPONENT OPERATION***/
		
		var priceField = document.getElementById("product-price");
		var quantityField = document.getElementById("stock-quantity");
		var inputQuantityField = document.getElementById("product-quantity");
		var expiryDateField = document.getElementById("product-exp-date");

		function addToChart() {
			if (currentProduct == null) {
				alert("Product is not specified!");
				return;
			}
			if (inputQuantityField.value*1 > quantityField.value*1) {
				alert("Quantity insufficient");
				return;
			}
			let ID = Math.floor(Math.random() * 1000);
			if (currentProductFlow != null && currentProductFlow.id != null) {
				ID = currentProductFlow.id;
				removeFromProductFlowsById(ID);
			}
			let productFlow = {
				"id" : ID,
				"product" : currentProduct,
				"price" : priceField.value,
				"count" : inputQuantityField.value,
				//"expiryDate" : expiryDateField.value,
				"flowReferenceId":stockIdField.value

			};

			productFlows.push(productFlow);
			populateProductFlow(productFlows);
			console.log("Product Flows", productFlows);
			currentProduct = null;
			currentProductFlow = null;
			clearProduct();
		}

		function clearProduct() {
			inputProductField.value = "";
			document.getElementById("unit-name").innerHTML = "";
			document.getElementById("product-dropdown").innerHTML = "";
			priceField.value = "";
			quantityField.value = "";
			inputQuantityField.value = "";
			expiryDateField.value = "";
			stockIdField.value = "";
		}

		function setCurrentProduct(entity, loadNewStock) {
			inputProductField.value = entity.product.name;
			document.getElementById("unit-name").innerHTML = entity.product.unit.name;
			currentProduct = entity.product;
			priceField.value = entity.product.price;
			inputQuantityField.value = entity.count;

			expiryDateField.value = entity.expiryDate;
			stockIdField.value = entity.id;

			//get remaining
			let ID = entity.id;
			if(entity.flowReferenceId != null){
				ID = entity.flowReferenceId;
			}
			if(loadNewStock)
				getStock(ID, false);

		}
		
		function  stockInfo(){
			getStock(stockIdField.value,true);
		}
		
		function  getStock(ID, updateProduct){
			var requestObject = {
					"productFlow" : {
						"id" : ID
					}
				}

				postReq("<spring:url value="/api/transaction/stockinfo" />",
						requestObject,
						function(xhr) {
							var response = (xhr.data);
							var code = response.code;
							if (code == "00") {
								quantityField.value = response.productFlowStock.remainingStock;
								if(updateProduct){
									setCurrentProduct(response.productFlowStock.productFlow, false);
								}
							} else {
								alert("server error");
							}
						});
		}

		function removeFromProductFlowsById(ID) {
			productFlowTable.innerHTML = "";
			for (let i = 0; i < productFlows.length; i++) {
				let productFlow = productFlows[i];
				if (productFlow.id == ID)
					productFlows.splice(i, 1);
			}
		}

		function populateProductFlow(productFlows) {
			productFlowTable.innerHTML = "";
			let totalPrice = 0;
			for (let i = 0; i < productFlows.length; i++) {
				let productFlow = productFlows[i];
				let row = document.createElement("tr");
				row.append(createCell((i * 1 + 1) + ""));
				row.append(createCell(productFlow.id));
				row.append(createCell(productFlow.product.name));
				row.append(createCell(productFlow.expiryDate));
				row.append(createCell(productFlow.count));
				row.append(createCell(productFlow.price));
				row.append(createCell(productFlow.flowReferenceId));
				
				let optionCell = createCell("");
				
				let btnEdit = createButton("edit-" + productFlow.id, "edit");
				let btnDelete = createButton("delete-" + productFlow.id,
						"delete");
				btnEdit.onclick = function() {
					setCurrentProductFlow(productFlow, true);
				}
				btnDelete.onclick = function() {
					if (!confirm("Are you sure wnat to delete?")) {
						return;
					}
					productFlows.splice(i, 1);
					populateProductFlow(productFlows);
				};
				optionCell.append(btnEdit);
				optionCell.append(btnDelete);
				row.append(optionCell);
				productFlowTable.append(row);

				totalPrice = totalPrice*1+(productFlow.price * productFlow.count);
				
				
			}

			totalPriceLabel.innerHTML = totalPrice;
		}

		function setCurrentProductFlow(entity) {
			currentProductFlow = entity;
		
			priceField.value = entity.price;
		//	quantityField.value = entity.productFlowStock.remainingStock;
			expiryDateField.value = entity.expiryDate;
			setCurrentProduct(entity, true);
		}
	</script>
</body>
</html>