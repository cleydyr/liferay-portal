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

import React from 'react';

import MultiBarChart from '../chart/bar/MultiBarChart.es';
import SimpleBarChart from '../chart/bar/SimpleBarChart.es';
import PieChart from '../chart/pie/PieChart.es';
import EmptyState from '../empty-state/EmptyState.es';
import List from '../list/List.es';
import Card from './Card.es';

const CHART_COMPONENTS = {
	List,
	MultiBarChart,
	PieChart,
	SimpleBarChart,
};

export default function CardList({data, fields}) {
	let hasCards = false;

	const cards = fields.map((field, index) => {
		const newData =
			data[field.parentFieldName]?.[field.name] ?? data[field.name] ?? {};
		const {
			chartComponentName,
			chartComponentProps = {},
			icon = '',
			title = '',
			summary = {},
			totalEntries,
		} = newData;

		if (!chartComponentName) {
			return null;
		}

		chartComponentProps.field = field;

		const {sumTotalValues} = chartComponentProps;

		field = {
			...field,
			icon,
			title,
		};

		const ChartComponent = CHART_COMPONENTS[chartComponentName];

		const chart = <ChartComponent {...chartComponentProps} />;

		hasCards = true;

		return (
			<Card
				field={field}
				index={index}
				key={index}
				summary={summary}
				totalEntries={totalEntries ? totalEntries : sumTotalValues}
			>
				{chart}
			</Card>
		);
	});

	if (!hasCards) {
		return <EmptyState />;
	}

	return cards;
}
