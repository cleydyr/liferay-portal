/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ClayButton from '@clayui/button';
import {ClayInput} from '@clayui/form';
import ClayModal, {useModal} from '@clayui/modal';
import React, {useContext, useEffect, useState} from 'react';

import App from '../../App.es';
import AppContext from '../../AppContext.es';
import {isDataLayoutEmpty} from '../../utils/dataLayoutVisitor.es';
import ModalWithEventPrevented from '../modal/ModalWithEventPrevented.es';
import useCreateFieldSet from './actions/useCreateFieldSet.es';
import useSaveAsFieldSet from './actions/useSaveAsFieldSet.es';

const ModalContent = ({defaultLanguageId, fieldSet, onClose, otherProps}) => {
	const [{appProps}] = useContext(AppContext);
	const [childrenContext, setChildrenContext] = useState({
		dispatch: () => {},
		state: {},
	});
	const [dataLayoutIsEmpty, setDataLayoutIsEmpty] = useState(true);
	const [name, setName] = useState('');

	useEffect(() => {
		if (fieldSet) {
			setName(fieldSet.name[defaultLanguageId]);
		}
	}, [defaultLanguageId, fieldSet]);

	useEffect(() => {
		if (childrenContext.state.dataLayout) {
			const {
				dataLayout: {dataLayoutPages},
			} = childrenContext.state;
			setDataLayoutIsEmpty(isDataLayoutEmpty(dataLayoutPages));
		}
	}, [childrenContext]);

	const createFieldSet = useCreateFieldSet({childrenContext});
	const saveAsFieldSet = useSaveAsFieldSet({
		childrenContext,
		defaultLanguageId,
		fieldSet,
		otherProps,
	});

	const saveFieldSet = fieldSet ? saveAsFieldSet : createFieldSet;

	const onSave = () => {
		saveFieldSet(name);
		onClose();
	};

	return (
		<>
			<ClayModal.Header>
				{fieldSet
					? Liferay.Language.get('edit-fieldset')
					: Liferay.Language.get('add-fieldset')}
			</ClayModal.Header>
			<ClayModal.Header withTitle={false}>
				<ClayInput.Group className="pl-4 pr-4">
					<ClayInput.GroupItem>
						<ClayInput
							aria-label={Liferay.Language.get(
								'untitled-fieldset'
							)}
							className="form-control-inline"
							disabled={!!fieldSet}
							onChange={({target: {value}}) => setName(value)}
							placeholder={Liferay.Language.get(
								'untitled-fieldset'
							)}
							type="text"
							value={name}
						/>
					</ClayInput.GroupItem>
				</ClayInput.Group>
			</ClayModal.Header>
			<ClayModal.Body>
				<div className="pl-4 pr-4">
					<App
						{...appProps}
						dataLayoutBuilderId={`${appProps.dataLayoutBuilderId}_2`}
						defaultLanguageId={defaultLanguageId}
						setChildrenContext={setChildrenContext}
						{...otherProps}
					/>
				</div>
			</ClayModal.Body>
			<ClayModal.Footer
				last={
					<ClayButton.Group spaced>
						<ClayButton displayType="secondary" onClick={onClose}>
							{Liferay.Language.get('cancel')}
						</ClayButton>
						<ClayButton
							disabled={!name || dataLayoutIsEmpty}
							onClick={onSave}
						>
							{Liferay.Language.get('save')}
						</ClayButton>
					</ClayButton.Group>
				}
			/>
		</>
	);
};

const FieldSetModal = ({isVisible, onClose, ...props}) => {
	const defaultLanguageId = Liferay.ThemeDisplay.getDefaultLanguageId();

	const {observer} = useModal({
		onClose,
	});

	if (!isVisible) {
		return null;
	}

	return (
		<ClayModal
			className="data-layout-builder-editor-modal fieldset-modal"
			observer={observer}
			size="full-screen"
		>
			<ModalContent
				defaultLanguageId={defaultLanguageId}
				onClose={onClose}
				{...props}
			/>
		</ClayModal>
	);
};

export default (props) => (
	<ModalWithEventPrevented>
		<FieldSetModal {...props} />
	</ModalWithEventPrevented>
);
