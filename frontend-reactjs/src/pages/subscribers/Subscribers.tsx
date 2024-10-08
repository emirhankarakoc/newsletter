import { http, httpError } from "@/assets/http";
import Footer from "@/components/Footer";
import Navigation from "@/components/Navigation";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Input,
  useDisclosure,
} from "@nextui-org/react";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export const Subscribers = () => {
  const { newsletterId } = useParams();
  const [subs, setSubs] = useState<Customer[]>();
  const [newsletter, setNewsletter] = useState<Newsletter>();
  const [selectedSub, setSelectedSub] = useState<Customer | null>(null); // Güncellenecek müşteri
  const [newName, setNewName] = useState(""); // Güncelleme formu için
  const [newEmail, setNewEmail] = useState(""); // Güncelleme formu için
  const [responseMessage, setResponseMessage] = useState<string>(""); // Genel response mesajı
  const deleteModal = useDisclosure(); // Silme modalı kontrolü
  const updateModal = useDisclosure(); // Güncelleme modalı kontrolü

  useEffect(() => {
    const handleGetNewsletter = async () => {
      try {
        const response = await http.get(`/newsletters/${newsletterId}`);
        setNewsletter(response.data);
      } catch (error) {
        httpError(error);
      }
    };

    const handleGetSubscribers = async () => {
      try {
        const response = await http.get(
          `/customers/newsletters/${newsletterId}`
        );
        setSubs(response.data);
      } catch (error) {
        httpError(error);
      }
    };

    handleGetNewsletter();
    handleGetSubscribers();
  }, [newsletterId]);

  const handleDelete = async () => {
    try {
      await http.delete(`/customers/${selectedSub?.id}`);
      const updatedSubs = subs?.filter((sub) => sub.id !== selectedSub?.id);
      setSubs(updatedSubs);
      setResponseMessage("Customer deleted successfully.");
      deleteModal.onOpenChange(); // Modalı kapat
    } catch (err) {
      updateModal.onOpenChange(); // Modalı kapat

      setResponseMessage(
        "Customer couldn't be deleted. Message: " + httpError(err)
      );
      httpError(err);
    } finally {
      setTimeout(() => setResponseMessage(""), 3000); // 3 saniye sonra mesajı temizle
    }
  };

  const handleUpdate = async () => {
    try {
      const body = {
        name: newName,
        email: newEmail,
      };

      const response = await http.put(`/customers/${selectedSub?.id}`, body);
      setResponseMessage("Customer updated successfully.");

      const updatedCustomer = response.data;
      const updatedSubs = subs?.map((sub) =>
        sub.id === updatedCustomer.id ? updatedCustomer : sub
      );
      setSubs(updatedSubs);
      updateModal.onOpenChange(); // Modalı kapat
    } catch (err) {
      console.log(httpError(err));

      updateModal.onOpenChange(); // Modalı kapat

      setResponseMessage(
        "Customer couldn't be updated. Message: " + httpError(err)
      );
      httpError(err);
    } finally {
      setTimeout(() => setResponseMessage(""), 3000); // 3 saniye sonra mesajı temizle
    }
  };

  const handleRegisteredDate = (date: string) => {
    let tarihVeSaat = date.split("T");
    console.log(tarihVeSaat[1].substring(0, 5)); // 19:25 mesela
    console.log(tarihVeSaat[0].substring(0, 10)); //2024 - 10-06 mesela
    let response =
      tarihVeSaat[1].substring(0, 5) + " " + tarihVeSaat[0].substring(0, 10);
    return response;
  };
  return (
    <div>
      <Navigation />
      <div className="grid grid-cols-4 px-24 min-h-[600px]">
        {/* musteriler tablosu */}
        <div className="col-span-4 p-10">
          <Table>
            <TableHeader>
              <TableColumn>Name</TableColumn>
              <TableColumn>Email</TableColumn>
              <TableColumn>Registered Date</TableColumn>
            </TableHeader>
            <TableBody>
              {subs?.map((sub, index) => (
                <TableRow key={index}>
                  <TableCell>{sub.name}</TableCell>
                  <TableCell>{sub.email}</TableCell>

                  <TableCell>
                    {sub.registerDate
                      ? handleRegisteredDate(sub.registerDate)
                      : "Bir problem var, tarih yok."}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </div>

      {/* Silme Modalı */}
      <Modal
        isOpen={deleteModal.isOpen}
        onOpenChange={() => {
          deleteModal.onOpenChange();
          setResponseMessage(""); // Modal kapandığında mesajı temizle
        }}
      >
        <ModalContent>
          <ModalHeader>Are you sure?</ModalHeader>
          <ModalBody>
            <p>Are you sure you want to delete this subscriber?</p>
          </ModalBody>
          <ModalFooter className="flex flex-col">
            <div>
              <Button color="danger" onPress={handleDelete}>
                Delete
              </Button>
              <Button
                color="danger"
                variant="ghost"
                onPress={deleteModal.onOpenChange}
              >
                Cancel
              </Button>
            </div>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Güncelleme Modalı */}
      <Modal
        isOpen={updateModal.isOpen}
        onOpenChange={() => {
          updateModal.onOpenChange();
          setResponseMessage(""); // Modal kapandığında mesajı temizle
        }}
      >
        <ModalContent>
          <ModalHeader>Update Subscriber</ModalHeader>
          <ModalBody>
            <Input
              label="Name"
              value={newName}
              onChange={(e) => setNewName(e.target.value)}
              placeholder="Enter new name"
            />
            <Input
              label="Email"
              value={newEmail}
              onChange={(e) => setNewEmail(e.target.value)}
              placeholder="Enter new email"
            />
          </ModalBody>
          <ModalFooter>
            <Button color="warning" onPress={handleUpdate}>
              Update
            </Button>
            <Button
              color="danger"
              variant="ghost"
              onPress={updateModal.onOpenChange}
            >
              Cancel
            </Button>
          </ModalFooter>
        </ModalContent>
      </Modal>

      {/* Genel response mesajı */}
      {responseMessage && (
        <div
          className={`fixed bottom-5 right-5 p-4 text-white rounded-lg shadow-lg transition duration-300 ${
            responseMessage.includes("successfully")
              ? "bg-green-500"
              : "bg-red-500"
          }`}
        >
          {responseMessage}
        </div>
      )}

      <Footer />
    </div>
  );
};
