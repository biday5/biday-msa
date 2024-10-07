//src/app/api/address/daumPost.tsx
import React from 'react';
import {
    Modal,
    ModalOverlay,
    ModalContent,
    ModalHeader,
    ModalFooter,
    ModalBody,
    ModalCloseButton,
    Button,
    useDisclosure
} from '@chakra-ui/react';
import DaumPostcode from 'react-daum-postcode';

interface AddressModalProps {
    isOpen: boolean;
    onClose: () => void;
    onCompletePost: (data: any) => void;
}

const AddressModal: React.FC<AddressModalProps> = ({ isOpen, onClose, onCompletePost }) => {
    return (
        <Modal isOpen={isOpen} onClose={onClose} size="md">
            <ModalOverlay />
            <ModalContent>
                <ModalHeader>주소검색</ModalHeader>
                <ModalCloseButton />
                <ModalBody>
                    <DaumPostcode onComplete={onCompletePost} />
                </ModalBody>
                <ModalFooter>
                    <Button colorScheme="blue" mr={3} onClick={onClose}>
                        확인
                    </Button>
                    <Button variant="ghost" onClick={onClose}>
                        취소
                    </Button>
                </ModalFooter>
            </ModalContent>
        </Modal>
    );
};

export default AddressModal;
